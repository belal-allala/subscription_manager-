package main.dao;

import main.entity.Abonnement;
import main.entity.AbonnementAvecEngagement;
import main.entity.AbonnementSansEngagement;
import main.entity.StatutAbonnement;
import main.util.DatabaseManager;


import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AbonnementDAO {

    public void create(Abonnement abonnement) {

        String sql="INSERT INTO abonnement(id, nomService, montantMensuel, dateDebut, dateFin, statut, typeAbonnement, dureeEngagementMois) VALUES(?,?,?,?,?,?,?,?)";

        try(Connection conn = DatabaseManager.getConnection(); 
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
            pstmt.setString(1, abonnement.getId());
            pstmt.setString(2, abonnement.getNomService());
            pstmt.setDouble(3, abonnement.getMontantMensuel());
            pstmt.setDate(4, Date.valueOf(abonnement.getDateDebut()));
            pstmt.setDate(5, Date.valueOf(abonnement.getDateFin()));
            pstmt.setString(6, abonnement.getStatut().name());

            if (abonnement instanceof AbonnementAvecEngagement) {
                pstmt.setString(7, "AVEC_ENGAGEMENT");
                pstmt.setInt(8, ((AbonnementAvecEngagement) abonnement).getDureeEngagementMois());
            } else {
                pstmt.setString(7, "SANS_ENGAGEMENT");
                pstmt.setNull(8, Types.INTEGER);
            }

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
    }

}