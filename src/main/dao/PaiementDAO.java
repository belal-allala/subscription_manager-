package main.dao;

import main.entity.Paiement;
import main.util.DatabaseManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaiementDAO {
    private final DatabaseManager dbManager;

    public PaiementDAO() {
        this.dbManager = new DatabaseManager();
    }

}