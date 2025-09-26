package main.entity;

import java.time.LocalDate;

public abstract class Abonnement {
    protected int id;
    protected LocalDate dateDebut;
    protected LocalDate dateFin;
    protected double montant;
    protected StatutAbonnement statut;

    // Constructeurs, getters et setters
    public Abonnement() {}

    public Abonnement(LocalDate dateDebut, LocalDate dateFin, double montant) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.montant = montant;
        this.statut = StatutAbonnement.ACTIF;
    }

    // Getters et Setters
    // TODO: Implement getters and setters
}