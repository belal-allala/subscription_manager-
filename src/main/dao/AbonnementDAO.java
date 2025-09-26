package main.dao;

import main.entity.Abonnement;
import main.util.DatabaseManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AbonnementDAO {
    private final DatabaseManager dbManager;

    public AbonnementDAO() {
        this.dbManager = new DatabaseManager();
    }

    // TODO: Implement CRUD operations for Abonnement
}