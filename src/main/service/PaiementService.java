package main.service;

import main.dao.PaiementDAO;
import main.entity.Paiement;
import java.util.List;

public class PaiementService {
    private final PaiementDAO paiementDAO;

    public PaiementService() {
        this.paiementDAO = new PaiementDAO();
    }

    // TODO: Implement business logic methods for payment management
}