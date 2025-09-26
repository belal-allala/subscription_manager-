package main.entity;

import java.time.LocalDate;

public abstract class Abonnement {
    protected String id;
    protected String nomService;
    protected LocalDate dateDebut;
    protected LocalDate dateFin;
    protected double montantMensuel;
    protected StatutAbonnement statut;

    public Abonnement() {}

    public Abonnement(String id, String nomService, LocalDate dateDebut, LocalDate dateFin, double montantMensuel) {
        this.id = id;
        this.nomService = nomService;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.montantMensuel = montantMensuel;
        this.statut = StatutAbonnement.ACTIF;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomService() {
        return nomService;
    }

    public void setNomService(String nomService) {
        this.nomService = nomService;
    }

    public double getMontantMensuel() {
        return montantMensuel;
    }

    public void setMontantMensuel(double montantMensuel) {
        this.montantMensuel = montantMensuel;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public StatutAbonnement getStatut() {
        return statut;
    }

    public void setStatut(StatutAbonnement statut) {
        this.statut = statut;
    }
   
}