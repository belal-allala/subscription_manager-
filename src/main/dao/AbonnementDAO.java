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

            if (abonnement.getDateFin() != null) {
                pstmt.setDate(5, Date.valueOf(abonnement.getDateFin()));
            } else {
                pstmt.setNull(5, Types.DATE);
            }

            pstmt.setString(6, abonnement.getStatut().name());

            if (abonnement instanceof AbonnementAvecEngagement) {
                AbonnementAvecEngagement aae = (AbonnementAvecEngagement) abonnement;
                pstmt.setString(7, "AVEC_ENGAGEMENT");
                pstmt.setInt(8, aae.getDureeEngagementMois());
            } else {
                pstmt.setString(7, "SANS_ENGAGEMENT");
                pstmt.setNull(8, Types.INTEGER);
            }

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la creation de l abonnement :");
            e.printStackTrace();
        }

    }

    public Optional<Abonnement> findById(String id) {
        String sql = "SELECT * FROM abonnement WHERE id = ?";

        try(Connection conn = DatabaseManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
                pstmt.setString(1, id);
                ResultSet rs= pstmt.executeQuery();

                if(rs.next()){
                    return Optional.of(mapResultSetToAbonnement(rs));
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de la recherche de l abonnement par ID :");
                e.printStackTrace();
            }

        return Optional.empty();
    }

    private Abonnement mapResultSetToAbonnement(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String nomService = rs.getString("nomService");
        Double montantMensuel = rs.getDouble("montantMensuel");
        LocalDate dateDebut = rs.getDate("dateDebut").toLocalDate();
        Date dateFin = null;
        if (rs.getDate("dateFin") != null) {
            dateFin = rs.getDate("dateFin");
        }
        StatutAbonnement statut = StatutAbonnement.valueOf(rs.getString("statut"));
        String typeAbonnement = rs.getString("typeAbonnement");

        if ("AVEC_ENGAGEMENT".equals(typeAbonnement)) {
            int dureeEngagementMois = rs.getInt("dureeEngagementMois");
            return new AbonnementAvecEngagement(id, nomService, montantMensuel, dateDebut, dateFin != null ? dateFin.toLocalDate() : null, statut, dureeEngagementMois);
        } else {
            return new AbonnementSansEngagement(id, nomService, dateDebut, dateFin != null ? dateFin.toLocalDate() : null, montantMensuel, statut);
        }
    }

    public List<Abonnement> findAll(){
        List<Abonnement> abonnements = new ArrayList<>();

        String sql="SELECT * FROM abonnement";

        try(Connection conn = DatabaseManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
                ResultSet rs = pstmt.executeQuery();
                while(rs.next()){
                    abonnements.add(mapResultSetToAbonnement(rs));
                }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recuperation de tous les abonnements :");
            e.printStackTrace();
        }
        return abonnements;
    }

    public boolean update(Abonnement abonnement){
        String sql = "UPDATE abonnement SET nomService = ?, montantMensuel = ?, dateDebut = ?, dateFin = ?, statut = ?, typeAbonnement = ?, dureeEngagementMois = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

                pstmt.setString(1, abonnement.getNomService());
                pstmt.setDouble(2, abonnement.getMontantMensuel());
                pstmt.setDate(3, Date.valueOf(abonnement.getDateDebut()));

                if (abonnement.getDateFin() != null) {
                    pstmt.setDate(4, Date.valueOf(abonnement.getDateFin()));
                } else {
                    pstmt.setNull(4, Types.DATE);
                }

                pstmt.setString(5, abonnement.getStatut().name());

                if (abonnement instanceof AbonnementAvecEngagement) {
                    AbonnementAvecEngagement aae = (AbonnementAvecEngagement) abonnement;
                    pstmt.setString(6, "AVEC_ENGAGEMENT");
                    pstmt.setInt(7, aae.getDureeEngagementMois());
                } else {
                    pstmt.setString(6, "SANS_ENGAGEMENT");
                    pstmt.setNull(7, Types.INTEGER);
                }

                pstmt.setString(8, abonnement.getId());

                int affectedRows = pstmt.executeUpdate();
                return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise a jour de l abonnement :");
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String id){

        String sql = "DELETE FROM abonnement WHERE id = ?";

        try(Connection conn = DatabaseManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
                pstmt.setString(1, id);
                int affectedRows = pstmt.executeUpdate();
                return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l abonnement :");
            e.printStackTrace();
            return false;
        }
    }

    public List<Abonnement> findAllActive(){
        List<Abonnement> abonnements = new ArrayList<>();
        String sql = "SELECT * FROM abonnement WHERE statut = ?";
        try(Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn .prepareStatement(sql)){
            pstmt.setString(1,"ACTIF" );
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                abonnements.add(mapResultSetToAbonnement(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recuperation des abonnements actifs :");
            e.printStackTrace();
        }
        return abonnements;
        
    }


    public AbonnementStats getAbonnementStats(){

        String sql = "SELECT typeAbonnement, COUNT(*) as count FROM Abonnement GROUP BY typeAbonnement";
        int avecEngagement = 0;
        int sansEngagement = 0;
        
        try(Connection conn = DatabaseManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                String type = rs.getString("typeAbonnement");
                int count = rs.getInt("count");
                if("AVEC_ENGAGEMENT".equals(type)){
                    avecEngagement = count;
                } else if ("SANS_ENGAGEMENT".equals(type)){
                    sansEngagement = count;
                }
            }
        } catch (SQLException e) {
                System.err.println("Erreur lors de la recuperation des statistiques des abonnements :");
                e.printStackTrace();
        }
        return new AbonnementStats(avecEngagement, sansEngagement);
    }

    public static class AbonnementStats {
        private final int avecEngagement;
        private final int sansEngagement;

        public AbonnementStats(int avecEngagement, int sansEngagement) {
            this.avecEngagement = avecEngagement;
            this.sansEngagement = sansEngagement;
        }

        public int getAvecEngagement() {
            return avecEngagement;
        }

        public int getSansEngagement() {
            return sansEngagement;
        }

        public int getTotal() {
            return avecEngagement + sansEngagement;
        }
    }

    
}