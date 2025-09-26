package main.service;

import main.dao.AbonnementDAO;
import main.entity.Abonnement;
import java.util.List;

public class AbonnementService {
    private final AbonnementDAO abonnementDAO;

    public AbonnementService() {
        this.abonnementDAO = new AbonnementDAO();
    }

    // TODO: Implement business logic methods for subscription management
}