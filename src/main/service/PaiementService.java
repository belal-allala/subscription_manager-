package main.service;

import main.dao.AbonnementDAO;
import main.dao.PaiementDAO;
import main.entity.*;
import main.util.IdGenerator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class PaiementService {
    private final PaiementDAO paiementDAO;
    private final AbonnementDAO abonnementDAO;

    public PaiementService() {
        this.paiementDAO = new PaiementDAO();
        this.abonnementDAO = new AbonnementDAO();
    }

    public Paiement enregistrerPaiement(String idAbonnement, LocalDate datePaiement, String typePaiement) {
        Optional<Abonnement> optAbonnement = abonnementDAO.findById(idAbonnement);
        if (!optAbonnement.isPresent()) {
            throw new IllegalArgumentException("Abonnement non trouve");
        }

        String idPaiement = IdGenerator.generateId();
        LocalDate dateEcheance = LocalDate.now().plusMonths(1);
        
        Paiement paiement = new Paiement(idPaiement, idAbonnement, dateEcheance, datePaiement, typePaiement);
        paiement.setStatut(StatutPaiement.VALIDE);
        
        if (!paiementDAO.create(paiement)) {
            throw new RuntimeException("Echec de l enregistrement du paiement");
        }
        
        return paiement;
    }

    public List<Paiement> genererEcheancesMensuelles(String idAbonnement, int nombreMois) {
        Optional<Abonnement> optAbonnement = abonnementDAO.findById(idAbonnement);
        if (!optAbonnement.isPresent()) {
            throw new IllegalArgumentException("Abonnement non trouve");
        }

        Abonnement abonnement = optAbonnement.get();
        List<Paiement> echeances = new ArrayList<>();
        LocalDate dateEcheance = LocalDate.now();

        for (int i = 0; i < nombreMois; i++) {
            dateEcheance = dateEcheance.plusMonths(1);
            String idPaiement = IdGenerator.generateId();
            
            Paiement paiement = new Paiement(idPaiement, idAbonnement, dateEcheance, null, "AUTOMATIQUE");
            paiement.setStatut(StatutPaiement.EN_ATTENTE);
            
            if (paiementDAO.create(paiement)) {
                echeances.add(paiement);
            }
        }

        return echeances;
    }

    public List<Paiement> getPaiementsImpayes() {
        return paiementDAO.findAll().stream()
            .filter(p -> p.getStatut() == StatutPaiement.EN_ATTENTE &&
                   p.getDateEcheance().isBefore(LocalDate.now()))
            .collect(Collectors.toList());
    }

    public double getMontantTotalImpayes() {
        return getPaiementsImpayes().stream()
            .map(p -> abonnementDAO.findById(p.getIdAbonnement()))
            .filter(Optional::isPresent)
            .mapToDouble(a -> a.get().getMontantMensuel())
            .sum();
    }

    public List<Paiement> getDerniersPaiements(String idAbonnement, int nombre) {
        return paiementDAO.findByAbonnementId(idAbonnement).stream()
            .filter(p -> p.getStatut() == StatutPaiement.VALIDE)
            .sorted((p1, p2) -> p2.getDatePaiement().compareTo(p1.getDatePaiement()))
            .limit(nombre)
            .collect(Collectors.toList());
    }

    public Map<String, Double> genererRapportMensuel(int mois, int annee) {
        return paiementDAO.findAll().stream()
            .filter(p -> {
                LocalDate date = p.getDatePaiement();
                return date != null && 
                       date.getMonthValue() == mois && 
                       date.getYear() == annee &&
                       p.getStatut() == StatutPaiement.VALIDE;
            })
            .collect(Collectors.groupingBy(
                Paiement::getIdAbonnement,
                Collectors.summingDouble(p -> {
                    Optional<Abonnement> abo = abonnementDAO.findById(p.getIdAbonnement());
                    return abo.map(Abonnement::getMontantMensuel).orElse(0.0);
                })
            ));
    }

    public double calculerTotalPaiements(String idAbonnement) {
        return paiementDAO.findByAbonnementId(idAbonnement).stream()
            .filter(p -> p.getStatut() == StatutPaiement.VALIDE)
            .mapToDouble(p -> {
                Optional<Abonnement> abo = abonnementDAO.findById(p.getIdAbonnement());
                return abo.map(Abonnement::getMontantMensuel).orElse(0.0);
            })
            .sum();
    }

    public void marquerPaiementRejete(String idPaiement, String raison) {
        Optional<Paiement> optPaiement = paiementDAO.findById(idPaiement);
        if (!optPaiement.isPresent()) {
            throw new IllegalArgumentException("Paiement non trouve");
        }

        Paiement paiement = optPaiement.get();
        paiement.setStatut(StatutPaiement.REJETE);
        paiementDAO.update(paiement);
    }

    public List<Paiement> listerTousPaiements() {
        return paiementDAO.findAll();
    }

    public Map<String, Object> genererRapportFinancier(int mois, int annee) {
        List<Paiement> paiementsDuMois = paiementDAO.findAll().stream()
            .filter(p -> {
                LocalDate date = p.getDateEcheance();
                return date.getMonthValue() == mois && date.getYear() == annee;
            })
            .collect(Collectors.toList());

        double totalPaye = paiementsDuMois.stream()
            .filter(p -> p.getStatut() == StatutPaiement.VALIDE)
            .mapToDouble(p -> {
                Optional<Abonnement> abo = abonnementDAO.findById(p.getIdAbonnement());
                return abo.map(Abonnement::getMontantMensuel).orElse(0.0);
            })
            .sum();

        double totalImpaye = paiementsDuMois.stream()
            .filter(p -> p.getStatut() == StatutPaiement.EN_ATTENTE)
            .mapToDouble(p -> {
                Optional<Abonnement> abo = abonnementDAO.findById(p.getIdAbonnement());
                return abo.map(Abonnement::getMontantMensuel).orElse(0.0);
            })
            .sum();

        int nombrePaiementsValides = (int) paiementsDuMois.stream()
            .filter(p -> p.getStatut() == StatutPaiement.VALIDE)
            .count();

        int nombrePaiementsRejetes = (int) paiementsDuMois.stream()
            .filter(p -> p.getStatut() == StatutPaiement.REJETE)
            .count();

        Map<String, Object> rapport = new HashMap<>();
        rapport.put("mois", mois);
        rapport.put("annee", annee);
        rapport.put("totalPaye", totalPaye);
        rapport.put("totalImpaye", totalImpaye);
        rapport.put("nombrePaiementsValides", nombrePaiementsValides);
        rapport.put("nombrePaiementsRejetes", nombrePaiementsRejetes);
        rapport.put("tauxReussite", nombrePaiementsValides * 100.0 / paiementsDuMois.size());

        return rapport;
    }
}