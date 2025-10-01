package main.service;

import main.dao.AbonnementDAO;
import main.entity.*;
import main.util.IdGenerator;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AbonnementService {
    private final AbonnementDAO abonnementDAO;

    public AbonnementService() {
        this.abonnementDAO = new AbonnementDAO();
    }

    public Abonnement creerAbonnementAvecEngagement(String nomService, double montantMensuel, 
            LocalDate dateDebut, int dureeEngagementMois) {

        if (montantMensuel <= 0) {
            throw new IllegalArgumentException("Le montant mensuel doit etre positif");
        }

        if (dateDebut == null) {
            throw new IllegalArgumentException("La date de debut est obligatoire");
        }

        if (dureeEngagementMois <= 0) {
            throw new IllegalArgumentException("La duree d engagement doit etre positive");
        }

        LocalDate dateFin = dateDebut.plusMonths(dureeEngagementMois);
        String id = IdGenerator.generateId();
        
        AbonnementAvecEngagement abonnement = new AbonnementAvecEngagement(
            id, nomService, montantMensuel, dateDebut, dateFin,  StatutAbonnement.ACTIF, dureeEngagementMois
        );
        
        abonnementDAO.create(abonnement);
        return abonnement;
    }

    public Abonnement creerAbonnementSansEngagement(String nomService, double montantMensuel, 
            LocalDate dateDebut) {

        if (montantMensuel <= 0) {
            throw new IllegalArgumentException("Le montant mensuel doit etre positif");
        }
        if (dateDebut == null) {
            throw new IllegalArgumentException("La date de debut est obligatoire");
        }

        String id = IdGenerator.generateId();
        AbonnementSansEngagement abonnement = new AbonnementSansEngagement(
            id, nomService, dateDebut, null, montantMensuel, StatutAbonnement.ACTIF
        );
        
        abonnementDAO.create(abonnement);
        return abonnement;
    }

    public void resilierAbonnement(String abonnementId, LocalDate dateResiliation) {
        Optional<Abonnement> optAbonnement = abonnementDAO.findById(abonnementId);
        if (!optAbonnement.isPresent()) {
            throw new IllegalArgumentException("Abonnement non trouve");
        }

        Abonnement abonnement = optAbonnement.get();
        
        if (abonnement instanceof AbonnementAvecEngagement) {
            AbonnementAvecEngagement abonnementEngagement = (AbonnementAvecEngagement) abonnement;
            LocalDate dateFinEngagement = abonnement.getDateDebut()
                .plusMonths(abonnementEngagement.getDureeEngagementMois());
            
            if (dateResiliation.isBefore(dateFinEngagement)) {
                throw new IllegalStateException("Impossible de resilier avant la fin de la periode d engagement");
            }
        }

        abonnement.setDateFin(dateResiliation);
        abonnement.setStatut(StatutAbonnement.RESILIE);
        abonnementDAO.update(abonnement);
    }

    public void suspendreAbonnement(String abonnementId) {
        Optional<Abonnement> optAbonnement = abonnementDAO.findById(abonnementId);
        if (!optAbonnement.isPresent()) {
            throw new IllegalArgumentException("Abonnement non trouve");
        }

        Abonnement abonnement = optAbonnement.get();
        abonnement.setStatut(StatutAbonnement.SUSPENDU);
        abonnementDAO.update(abonnement);
    }

    public void reactiverAbonnement(String abonnementId) {
        Optional<Abonnement> optAbonnement = abonnementDAO.findById(abonnementId);
        if (!optAbonnement.isPresent()) {
            throw new IllegalArgumentException("Abonnement non trouvé");
        }

        Abonnement abonnement = optAbonnement.get();
        if (abonnement.getStatut() != StatutAbonnement.SUSPENDU) {
            throw new IllegalStateException("L abonnement n est pas suspendu");
        }
        
        abonnement.setStatut(StatutAbonnement.ACTIF);
        abonnementDAO.update(abonnement);
    }

    public List<Abonnement> listerAbonnementsActifs() {
        return abonnementDAO.findAllActive().stream()
                .collect(Collectors.toList());
    }

    public List<Abonnement> listerTousAbonnements() {
        return abonnementDAO.findAll();
    }

    public List<Abonnement> listerAbonnementsExpires() {
        LocalDate dateActuelle = LocalDate.now();
        return abonnementDAO.findAll().stream()
            .filter(a -> a.getDateFin() != null && a.getDateFin().isBefore(dateActuelle))
            .collect(Collectors.toList());
    }

    public double calculerMontantTotalMensuel() {
        return listerAbonnementsActifs().stream()
            .mapToDouble(Abonnement::getMontantMensuel)
            .sum();
    }

    public long calculerDureeAbonnement(String abonnementId) {
        Optional<Abonnement> optAbonnement = abonnementDAO.findById(abonnementId);
        if (!optAbonnement.isPresent()) {
            throw new IllegalArgumentException("Abonnement non trouve");
        }

        Abonnement abonnement = optAbonnement.get();
        LocalDate fin = abonnement.getDateFin() != null ? 
            abonnement.getDateFin() : LocalDate.now();
            
        return ChronoUnit.MONTHS.between(abonnement.getDateDebut(), fin);
    }

    public boolean modifierAbonnement(Abonnement abonnement) {
        if (abonnement == null) {
            throw new IllegalArgumentException("L'abonnement ne peut pas etre null");
        }
        
        if (abonnement.getMontantMensuel() <= 0) {
            throw new IllegalArgumentException("Le montant mensuel doit etre positif");
        }
        
        return abonnementDAO.update(abonnement);
    }

}