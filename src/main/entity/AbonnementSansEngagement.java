package main.entity;

import java.time.LocalDate;

public class AbonnementSansEngagement extends Abonnement {

    public AbonnementSansEngagement() {
        super();
    }

    public AbonnementSansEngagement(String id, String nomService, LocalDate dateDebut, LocalDate dateFin, double montant) {
        super(id, nomService, dateDebut, dateFin, montant);
    }
}