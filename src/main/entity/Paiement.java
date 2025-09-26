package main.entity;

import java.time.LocalDate;

public class Paiement {
    private int id;
    private int abonnementId;
    private double montant;
    private LocalDate datePaiement;
    private StatutPaiement statut;

    public Paiement() {}

    public Paiement(int abonnementId, double montant, LocalDate datePaiement) {
        this.abonnementId = abonnementId;
        this.montant = montant;
        this.datePaiement = datePaiement;
        this.statut = StatutPaiement.EN_ATTENTE;
    }

    // Getters et Setters
    // TODO: Implement getters and setters
}