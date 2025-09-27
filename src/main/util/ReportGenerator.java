package main.util;

import main.entity.Abonnement;
import main.entity.Paiement;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class ReportGenerator {
    private static final String REPORTS_DIR = "reports";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        try {
            Path reportsPath = Paths.get(REPORTS_DIR);
            if (!Files.exists(reportsPath)) {
                Files.createDirectories(reportsPath);
            }
        } catch (IOException e) {
            LogManager.logError("Erreur lors de la création du dossier reports", e);
        }
    }

    public static void generateAbonnementsCSV(List<Abonnement> abonnements, String fileName) {
        Path filePath = Paths.get(REPORTS_DIR, fileName + ".csv");
        
        try (FileWriter writer = new FileWriter(filePath.toFile())) {
            // En-têtes
            writer.write("ID,Service,Montant Mensuel,Date Début,Date Fin,Statut,Type\n");
            
            // Données
            for (Abonnement abonnement : abonnements) {
                writer.write(String.format("%s,%s,%.2f,%s,%s,%s,%s\n",
                    abonnement.getId(),
                    abonnement.getNomService().replace(",", ";"),
                    abonnement.getMontantMensuel(),
                    abonnement.getDateDebut().format(DATE_FORMAT),
                    abonnement.getDateFin() != null ? abonnement.getDateFin().format(DATE_FORMAT) : "",
                    abonnement.getStatut(),
                    abonnement instanceof main.entity.AbonnementAvecEngagement ? "AVEC_ENGAGEMENT" : "SANS_ENGAGEMENT"
                ));
            }
            
            LogManager.logInfo("Rapport CSV des abonnements généré : " + fileName);
        } catch (IOException e) {
            LogManager.logError("Erreur lors de la génération du rapport CSV des abonnements", e);
            throw new RuntimeException("Erreur lors de la génération du rapport", e);
        }
    }

    public static void generatePaiementsCSV(List<Paiement> paiements, String fileName) {
        Path filePath = Paths.get(REPORTS_DIR, fileName + ".csv");
        
        try (FileWriter writer = new FileWriter(filePath.toFile())) {
            // En-têtes
            writer.write("ID Paiement,ID Abonnement,Date Échéance,Date Paiement,Type,Statut\n");
            
            // Données
            for (Paiement paiement : paiements) {
                writer.write(String.format("%s,%s,%s,%s,%s,%s\n",
                    paiement.getIdPaiement(),
                    paiement.getIdAbonnement(),
                    paiement.getDateEcheance().format(DATE_FORMAT),
                    paiement.getDatePaiement() != null ? paiement.getDatePaiement().format(DATE_FORMAT) : "",
                    paiement.getTypePaiement(),
                    paiement.getStatut()
                ));
            }
            
            LogManager.logInfo("Rapport CSV des paiements généré : " + fileName);
        } catch (IOException e) {
            LogManager.logError("Erreur lors de la génération du rapport CSV des paiements", e);
            throw new RuntimeException("Erreur lors de la génération du rapport", e);
        }
    }

    public static void generateRapportFinancierJSON(Map<String, Double> rapport, String fileName) {
        Path filePath = Paths.get(REPORTS_DIR, fileName + ".json");
        
        try {
            ObjectNode rootNode = objectMapper.createObjectNode();
            ObjectNode rapportNode = objectMapper.createObjectNode();
            
            rapport.forEach(rapportNode::put);
            
            rootNode.put("date_generation", java.time.LocalDate.now().format(DATE_FORMAT));
            rootNode.set("rapport", rapportNode);
            rootNode.put("total", rapport.values().stream().mapToDouble(Double::doubleValue).sum());
            
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(filePath.toFile(), rootNode);
            
            LogManager.logInfo("Rapport JSON généré : " + fileName);
        } catch (IOException e) {
            LogManager.logError("Erreur lors de la génération du rapport JSON", e);
            throw new RuntimeException("Erreur lors de la génération du rapport", e);
        }
    }

    public static void generateAbonnementsJSON(List<Abonnement> abonnements, String fileName) {
        Path filePath = Paths.get(REPORTS_DIR, fileName + ".json");
        
        try {
            ObjectNode rootNode = objectMapper.createObjectNode();
            ArrayNode abonnementsNode = rootNode.putArray("abonnements");
            
            for (Abonnement abonnement : abonnements) {
                ObjectNode abonnementNode = objectMapper.createObjectNode();
                abonnementNode.put("id", abonnement.getId());
                abonnementNode.put("service", abonnement.getNomService());
                abonnementNode.put("montantMensuel", abonnement.getMontantMensuel());
                abonnementNode.put("dateDebut", abonnement.getDateDebut().format(DATE_FORMAT));
                if (abonnement.getDateFin() != null) {
                    abonnementNode.put("dateFin", abonnement.getDateFin().format(DATE_FORMAT));
                }
                abonnementNode.put("statut", abonnement.getStatut().toString());
                abonnementNode.put("type", abonnement instanceof main.entity.AbonnementAvecEngagement ? 
                    "AVEC_ENGAGEMENT" : "SANS_ENGAGEMENT");
                
                abonnementsNode.add(abonnementNode);
            }
            
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(filePath.toFile(), rootNode);
            
            LogManager.logInfo("Rapport JSON des abonnements généré : " + fileName);
        } catch (IOException e) {
            LogManager.logError("Erreur lors de la génération du rapport JSON des abonnements", e);
            throw new RuntimeException("Erreur lors de la génération du rapport", e);
        }
    }
}