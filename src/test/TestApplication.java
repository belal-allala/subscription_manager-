package test;

import main.entity.Abonnement;
import main.entity.Paiement;
import main.service.AbonnementService;
import main.service.PaiementService;
import main.util.LogManager;
import main.util.ReportGenerator;

import java.time.LocalDate;
import java.util.List;

public class TestApplication {
    private static final AbonnementService abonnementService = new AbonnementService();
    private static final PaiementService paiementService = new PaiementService();
    private static final LogManager logManager = LogManager.getInstance();
    private static final ReportGenerator reportGenerator = new ReportGenerator();

    public static void main(String[] args) {
        System.out.println("=== Test de l'application de gestion d'abonnements ===\n");

        try {
            // Test 1: Création d'un abonnement avec engagement
            System.out.println("1. Test création d'un abonnement avec engagement");
            Abonnement abo1 = abonnementService.creerAbonnementAvecEngagement(
                "Netflix Premium", 15.99, LocalDate.now(), 12
            );
            System.out.println("✓ Abonnement créé avec ID : " + abo1.getId());

            // Test 2: Création d'un abonnement sans engagement
            System.out.println("\n2. Test création d'un abonnement sans engagement");
            Abonnement abo2 = abonnementService.creerAbonnementSansEngagement(
                "Spotify", 9.99, LocalDate.now()
            );
            System.out.println("✓ Abonnement créé avec ID : " + abo2.getId());

            // Test 3: Enregistrement d'un paiement
            System.out.println("\n3. Test enregistrement d'un paiement");
            Paiement paiement = paiementService.enregistrerPaiement(
                abo1.getId(), LocalDate.now(), "CB"
            );
            System.out.println("✓ Paiement enregistré avec ID : " + paiement.getIdPaiement());

            // Test 4: Génération d'échéances mensuelles
            System.out.println("\n4. Test génération d'échéances");
            List<Paiement> echeances = paiementService.genererEcheancesMensuelles(abo1.getId(), 3);
            System.out.println("✓ " + echeances.size() + " échéances générées");

            // Test 5: Export CSV des abonnements
            System.out.println("\n5. Test export CSV des abonnements");
            String csvFile = reportGenerator.generateAbonnementsCsv(abonnementService.listerTousAbonnements());
            System.out.println("✓ Fichier CSV généré : " + csvFile);

            // Test 6: Export JSON des abonnements
            System.out.println("\n6. Test export JSON des abonnements");
            String jsonFile = reportGenerator.generateAbonnementsJson(abonnementService.listerTousAbonnements());
            System.out.println("✓ Fichier JSON généré : " + jsonFile);

            // Test 7: Rapport financier
            System.out.println("\n7. Test génération rapport financier");
            String rapportFile = reportGenerator.generateRapportFinancierJson(
                paiementService.genererRapportFinancier(LocalDate.now().getMonthValue(), LocalDate.now().getYear())
            );
            System.out.println("✓ Rapport financier généré : " + rapportFile);

            System.out.println("\nTous les tests ont été effectués avec succès !");

        } catch (Exception e) {
            System.err.println("\n❌ Erreur lors des tests : " + e.getMessage());
            e.printStackTrace();
        }
    }
}