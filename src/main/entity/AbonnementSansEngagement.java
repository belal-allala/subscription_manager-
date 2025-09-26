package main.entity;

import java.time.LocalDate;

public class AbonnementSansEngagement extends Abonnement {
    private double fraisResiliation;

    public AbonnementSansEngagement() {
        super();
    }

    public AbonnementSansEngagement(LocalDate dateDebut, LocalDate dateFin, double montant, double fraisResiliation) {
        super(dateDebut, dateFin, montant);
        this.fraisResiliation = fraisResiliation;
    }

    // Getters et Setters spécifiques
    // TODO: Implement getters and setters
}