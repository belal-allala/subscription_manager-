package main.entity;

import java.time.LocalDate;

public class AbonnementAvecEngagement extends Abonnement {
    private int dureeEngagement; // en mois

    public AbonnementAvecEngagement() {
        super();
    }

    public AbonnementAvecEngagement(LocalDate dateDebut, LocalDate dateFin, double montant, int dureeEngagement) {
        super(dateDebut, dateFin, montant);
        this.dureeEngagement = dureeEngagement;
    }

    // Getters et Setters spécifiques
    // TODO: Implement getters and setters
}