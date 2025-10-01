package main.dao;

import main.entity.Paiement;
import main.entity.StatutPaiement;
import main.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

public class PaiementDAO {

    public boolean create(Paiement paiement){
        String sql = "INSERT INTO Paiement (idPaiement, idAbonnement, dateEcheance, datePaiement, typePaiement, statut) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try(Connection conn = DatabaseManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, paiement.getIdPaiement());
                pstmt.setString(2, paiement.getIdAbonnement());
                pstmt.setDate(3, Date.valueOf(paiement.getDateEcheance()));

                if (paiement.getDatePaiement() != null) {
                    pstmt.setDate(4, Date.valueOf(paiement.getDatePaiement()));
                } else {
                    pstmt.setNull(4, Types.DATE);
                }

                pstmt.setString(5, paiement.getTypePaiement());
                pstmt.setString(6, paiement.getStatut().name());
                int affectedRows = pstmt.executeUpdate();
                return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la creation du paiement :");
            e.printStackTrace();
            return false;
        }
    }

    public Optional<Paiement> findById(String idPaiement){
        String sql = "SELECT * FROM Paiement WHERE idPaiement = ?";

        try(Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, idPaiement);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                return Optional.of(mapResultSetToPaiement(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du paiement par ID :");
            e.printStackTrace();
        }

        return Optional.empty();
    }

    private Paiement mapResultSetToPaiement(ResultSet rs) throws SQLException {
        String idPaiement = rs.getString("idPaiement");
        String idAbonnement = rs.getString("idAbonnement");
        LocalDate dateEcheance = rs.getDate("dateEcheance") != null ? rs.getDate("dateEcheance").toLocalDate() : null;
        LocalDate datePaiement = rs.getDate("datePaiement") != null ? rs.getDate("datePaiement").toLocalDate() : null;
        String typePaiement = rs.getString("typePaiement");
        StatutPaiement statut = StatutPaiement.valueOf(rs.getString("statut"));

        return new Paiement(idPaiement, idAbonnement, dateEcheance, datePaiement, typePaiement);
    }

    public List<Paiement> findAll(){

        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT * FROM Paiement";

        try(Connection conn = DatabaseManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

                ResultSet rs = pstmt.executeQuery();

                while(rs.next()){
                    paiements.add(mapResultSetToPaiement(rs));
                }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de tous les paiements :");
            e.printStackTrace();
        }
        return paiements;
    }

    public boolean update(Paiement paiement){
        String sql = "UPDATE Paiement SET idAbonnement = ?, dateEcheance = ?, datePaiement = ?, typePaiement = ?, statut = ? " +
                     "WHERE idPaiement = ?";

        try(Connection conn = DatabaseManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, paiement.getIdAbonnement());

                if(paiement.getDateEcheance() != null) {
                    pstmt.setDate(2, Date.valueOf(paiement.getDateEcheance()));
                } else {
                    pstmt.setNull(2, Types.DATE);
                }

                if (paiement.getDatePaiement() != null) {
                    pstmt.setDate(3, Date.valueOf(paiement.getDatePaiement()));
                } else {
                    pstmt.setNull(3, Types.DATE);
                }

                pstmt.setString(4, paiement.getTypePaiement());
                pstmt.setString(5, paiement.getStatut().name());
                pstmt.setString(6, paiement.getIdPaiement());

                int affectedRows = pstmt.executeUpdate();
                return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise a jour du paiement :");
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String idPaiement){
        String sql = "DELETE FROM Paiement WHERE idPaiement = ?";

        try(Connection conn = DatabaseManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, idPaiement);
                int affectedRows = pstmt.executeUpdate();
                return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du paiement :");
            e.printStackTrace();
            return false;
        }
    }

    public List<Paiement> findByAbonnementId(String idAbonnement){
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT * FROM Paiement WHERE idAbonnement = ?";

        try(Connection conn = DatabaseManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, idAbonnement);
                ResultSet rs = pstmt.executeQuery();

                while(rs.next()){
                    paiements.add(mapResultSetToPaiement(rs));
                }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des paiements par ID d'abonnement :");
            e.printStackTrace();
        }
        return paiements;
    }

    public List<Paiement> findByPeriode(LocalDate startDate, LocalDate endDate){
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT * FROM Paiement WHERE dateEcheance BETWEEN ? AND ?";

        try(Connection conn = DatabaseManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setDate(1, Date.valueOf(startDate));
                pstmt.setDate(2, Date.valueOf(endDate));
                ResultSet rs = pstmt.executeQuery();

                while(rs.next()){
                    paiements.add(mapResultSetToPaiement(rs));
                }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des paiements par période :");
            e.printStackTrace();
        }
        return paiements;
    }

    public List<Paiement> findPaiementsAEcheance(){
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT * FROM Paiement WHERE dateEcheance BETWEEN CURRENT_DATE AND CURRENT_DATE + 7";

        try(Connection conn = DatabaseManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, StatutPaiement.EN_ATTENTE.name());
                ResultSet rs = pstmt.executeQuery();

                while(rs.next()){
                    paiements.add(mapResultSetToPaiement(rs));
                }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des paiements à échéance :");
            e.printStackTrace();
        }
        return paiements;
    }

    public StatutPaiementStats getStatutPaiementStats() {
        StatutPaiementStats stats = new StatutPaiementStats();
        String sql = "SELECT statut, COUNT(*) as count FROM Paiement GROUP BY statut";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                 StatutPaiement statut = StatutPaiement.valueOf(rs.getString("statut"));
                int count = rs.getInt("count");
                stats.addCount(statut, count);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des statistiques de statut de paiement :");
            e.printStackTrace();
        }
        return stats;
    }

    private static class StatutPaiementStats {
        private final java.util.Map<StatutPaiement, Integer> stats = new java.util.HashMap<>();

        public void addCount(StatutPaiement statut, int count) {
            stats.put(statut, count);
        }

        public int getCount(StatutPaiement statut) {
            return stats.getOrDefault(statut, 0);
        }

        public java.util.Map<StatutPaiement, Integer> getAllStats() {
            return stats;
        }

        public int getTotal() {
            return stats.values().stream().mapToInt(Integer::intValue).sum();
        }
    }
}