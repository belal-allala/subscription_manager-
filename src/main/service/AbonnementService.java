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
        // Validation des entrées
        if (montantMensuel <= 0) {
            throw new IllegalArgumentException("Le montant mensuel doit être positif");
        }
        if (dateDebut == null) {
            throw new IllegalArgumentException("La date de début est obligatoire");
        }
        if (dureeEngagementMois <= 0) {
            throw new IllegalArgumentException("La durée d'engagement doit être positive");
        }

        LocalDate dateFin = dateDebut.plusMonths(dureeEngagementMois);
        String id = IdGenerator.generateId();
        
        AbonnementAvecEngagement abonnement = new AbonnementAvecEngagement(
            id, nomService, montantMensuel, dateDebut, dateFin, 
            StatutAbonnement.ACTIF, dureeEngagementMois
        );
        
        abonnementDAO.create(abonnement);
        return abonnement;
    }

    public Abonnement creerAbonnementSansEngagement(String nomService, double montantMensuel, 
            LocalDate dateDebut) {
        // Validation des entrées
        if (montantMensuel <= 0) {
            throw new IllegalArgumentException("Le montant mensuel doit être positif");
        }
        if (dateDebut == null) {
            throw new IllegalArgumentException("La date de début est obligatoire");
        }

        String id = IdGenerator.generateId();
        AbonnementSansEngagement abonnement = new AbonnementSansEngagement(
            id, nomService, dateDebut, null, montantMensuel
        );
        
        abonnementDAO.create(abonnement);
        return abonnement;
    }

    public void resilierAbonnement(String abonnementId, LocalDate dateResiliation) {
        Optional<Abonnement> optAbonnement = abonnementDAO.findById(abonnementId);
        if (!optAbonnement.isPresent()) {
            throw new IllegalArgumentException("Abonnement non trouvé");
        }

        Abonnement abonnement = optAbonnement.get();
        
        // Vérification des conditions de résiliation
        if (abonnement instanceof AbonnementAvecEngagement) {
            AbonnementAvecEngagement abonnementEngagement = (AbonnementAvecEngagement) abonnement;
            LocalDate dateFinEngagement = abonnement.getDateDebut()
                .plusMonths(abonnementEngagement.getDureeEngagementMois());
            
            if (dateResiliation.isBefore(dateFinEngagement)) {
                throw new IllegalStateException("Impossible de résilier avant la fin de la période d'engagement");
            }
        }

        abonnement.setDateFin(dateResiliation);
        abonnement.setStatut(StatutAbonnement.RESILIE);
        abonnementDAO.update(abonnement);
    }

    public void suspendreAbonnement(String abonnementId) {
        Optional<Abonnement> optAbonnement = abonnementDAO.findById(abonnementId);
        if (!optAbonnement.isPresent()) {
            throw new IllegalArgumentException("Abonnement non trouvé");
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
            throw new IllegalStateException("L'abonnement n'est pas suspendu");
        }
        
        abonnement.setStatut(StatutAbonnement.ACTIF);
        abonnementDAO.update(abonnement);
    }

    public List<Abonnement> listerAbonnementsActifs() {
        return abonnementDAO.findAll().stream()
            .filter(a -> a.getStatut() == StatutAbonnement.ACTIF)
            .collect(Collectors.toList());
    }

    public List<Abonnement> listerAbonnementsExpires() {
        LocalDate aujourd'hui = LocalDate.now();
        return abonnementDAO.findAll().stream()
            .filter(a -> a.getDateFin() != null && a.getDateFin().isBefore(aujourd'hui))
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
            throw new IllegalArgumentException("Abonnement non trouvé");
        }

        Abonnement abonnement = optAbonnement.get();
        LocalDate fin = abonnement.getDateFin() != null ? 
            abonnement.getDateFin() : LocalDate.now();
            
        return ChronoUnit.MONTHS.between(abonnement.getDateDebut(), fin);
    }