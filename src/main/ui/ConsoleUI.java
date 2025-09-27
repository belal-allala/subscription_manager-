package main.ui;

import main.entity.*;
import main.service.AbonnementService;
import main.service.PaiementService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ConsoleUI {
    private final AbonnementService abonnementService;
    private final PaiementService paiementService;
    private final Scanner scanner;
    private final DateTimeFormatter dateFormat;
    private boolean running;

    public ConsoleUI() {
        this.abonnementService = new AbonnementService();
        this.paiementService = new PaiementService();
        this.scanner = new Scanner(System.in);
        this.dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.running = true;
    }

    public void start() {
        System.out.println("Bienvenue dans le gestionnaire d'abonnements");
        
        while (running) {
            afficherMenu();
            int choix = lireChoix();
            traiterChoix(choix);
        }
    }

    private void afficherMenu() {
        System.out.println("\n=== MENU PRINCIPAL ===");
        System.out.println("1. Gestion des Abonnements");
        System.out.println("2. Gestion des Paiements");
        System.out.println("3. Rapports et Statistiques");
        System.out.println("0. Quitter");
        System.out.print("Votre choix : ");
    }

    private void afficherMenuAbonnements() {
        System.out.println("\n=== GESTION DES ABONNEMENTS ===");
        System.out.println("1. Créer un abonnement avec engagement");
        System.out.println("2. Créer un abonnement sans engagement");
        System.out.println("3. Modifier un abonnement");
        System.out.println("4. Résilier un abonnement");
        System.out.println("5. Suspendre un abonnement");
        System.out.println("6. Réactiver un abonnement");
        System.out.println("7. Liste des abonnements actifs");
        System.out.println("0. Retour");
        System.out.print("Votre choix : ");
    }

    private void afficherMenuPaiements() {
        System.out.println("\n=== GESTION DES PAIEMENTS ===");
        System.out.println("1. Enregistrer un paiement");
        System.out.println("2. Afficher les paiements d'un abonnement");
        System.out.println("3. Afficher les 5 derniers paiements");
        System.out.println("4. Générer les échéances mensuelles");
        System.out.println("0. Retour");
        System.out.print("Votre choix : ");
    }

    private void afficherMenuRapports() {
        System.out.println("\n=== RAPPORTS ET STATISTIQUES ===");
        System.out.println("1. Montant total des impayés");
        System.out.println("2. Liste des paiements impayés");
        System.out.println("3. Rapport mensuel");
        System.out.println("4. Total payé pour un abonnement");
        System.out.println("0. Retour");
        System.out.print("Votre choix : ");
    }

    private int lireChoix() {
        while (!scanner.hasNextInt()) {
            System.out.println("Veuillez entrer un nombre valide.");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private void traiterChoix(int choix) {
        switch (choix) {
            case 1:
                gererAbonnements();
                break;
            case 2:
                gererPaiements();
                break;
            case 3:
                gererRapports();
                break;
            case 0:
                running = false;
                System.out.println("Au revoir !");
                break;
            default:
                System.out.println("Choix invalide");
        }
    }

    private void gererAbonnements() {
        afficherMenuAbonnements();
        int choix = lireChoix();
        scanner.nextLine(); // Consommer la nouvelle ligne

        try {
            switch (choix) {
                case 1:
                    creerAbonnementAvecEngagement();
                    break;
                case 2:
                    creerAbonnementSansEngagement();
                    break;
                case 3:
                    // TODO: Implémenter la modification
                    break;
                case 4:
                    resilierAbonnement();
                    break;
                case 5:
                    suspendreAbonnement();
                    break;
                case 6:
                    reactiverAbonnement();
                    break;
                case 7:
                    listerAbonnementsActifs();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Choix invalide");
            }
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    private void gererPaiements() {
        afficherMenuPaiements();
        int choix = lireChoix();
        scanner.nextLine(); // Consommer la nouvelle ligne

        try {
            switch (choix) {
                case 1:
                    enregistrerPaiement();
                    break;
                case 2:
                    afficherPaiementsAbonnement();
                    break;
                case 3:
                    afficherDerniersPaiements();
                    break;
                case 4:
                    genererEcheances();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Choix invalide");
            }
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    private void gererRapports() {
        afficherMenuRapports();
        int choix = lireChoix();
        scanner.nextLine(); // Consommer la nouvelle ligne

        try {
            switch (choix) {
                case 1:
                    afficherMontantTotalImpayes();
                    break;
                case 2:
                    listerPaiementsImpayes();
                    break;
                case 3:
                    genererRapportMensuel();
                    break;
                case 4:
                    afficherTotalPaiements();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Choix invalide");
            }
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    // Méthodes pour les abonnements
    private void creerAbonnementAvecEngagement() {
        System.out.print("Nom du service : ");
        String nomService = scanner.nextLine();
        
        System.out.print("Montant mensuel : ");
        double montant = scanner.nextDouble();
        scanner.nextLine();
        
        System.out.print("Date de début (dd/MM/yyyy) : ");
        LocalDate dateDebut = LocalDate.parse(scanner.nextLine(), dateFormat);
        
        System.out.print("Durée d'engagement (en mois) : ");
        int dureeEngagement = scanner.nextInt();
        
        Abonnement abonnement = abonnementService.creerAbonnementAvecEngagement(
            nomService, montant, dateDebut, dureeEngagement);
        
        System.out.println("Abonnement créé avec succès. ID : " + abonnement.getId());
    }

    private void creerAbonnementSansEngagement() {
        System.out.print("Nom du service : ");
        String nomService = scanner.nextLine();
        
        System.out.print("Montant mensuel : ");
        double montant = scanner.nextDouble();
        scanner.nextLine();
        
        System.out.print("Date de début (dd/MM/yyyy) : ");
        LocalDate dateDebut = LocalDate.parse(scanner.nextLine(), dateFormat);
        
        Abonnement abonnement = abonnementService.creerAbonnementSansEngagement(
            nomService, montant, dateDebut);
        
        System.out.println("Abonnement créé avec succès. ID : " + abonnement.getId());
    }

    private void resilierAbonnement() {
        System.out.print("ID de l'abonnement : ");
        String id = scanner.nextLine();
        
        System.out.print("Date de résiliation (dd/MM/yyyy) : ");
        LocalDate dateResiliation = LocalDate.parse(scanner.nextLine(), dateFormat);
        
        abonnementService.resilierAbonnement(id, dateResiliation);
        System.out.println("Abonnement résilié avec succès");
    }

    private void suspendreAbonnement() {
        System.out.print("ID de l'abonnement : ");
        String id = scanner.nextLine();
        
        abonnementService.suspendreAbonnement(id);
        System.out.println("Abonnement suspendu avec succès");
    }

    private void reactiverAbonnement() {
        System.out.print("ID de l'abonnement : ");
        String id = scanner.nextLine();
        
        abonnementService.reactiverAbonnement(id);
        System.out.println("Abonnement réactivé avec succès");
    }

    private void listerAbonnementsActifs() {
        List<Abonnement> abonnements = abonnementService.listerAbonnementsActifs();
        if (abonnements.isEmpty()) {
            System.out.println("Aucun abonnement actif");
            return;
        }
        
        System.out.println("\nListe des abonnements actifs :");
        abonnements.forEach(a -> System.out.printf(
            "ID: %s, Service: %s, Montant: %.2f€/mois%n",
            a.getId(), a.getNomService(), a.getMontantMensuel()
        ));
    }

    // Méthodes pour les paiements
    private void enregistrerPaiement() {
        System.out.print("ID de l'abonnement : ");
        String idAbonnement = scanner.nextLine();
        
        System.out.print("Date du paiement (dd/MM/yyyy) : ");
        LocalDate datePaiement = LocalDate.parse(scanner.nextLine(), dateFormat);
        
        System.out.print("Type de paiement : ");
        String typePaiement = scanner.nextLine();
        
        Paiement paiement = paiementService.enregistrerPaiement(
            idAbonnement, datePaiement, typePaiement);
        
        System.out.println("Paiement enregistré avec succès. ID : " + paiement.getIdPaiement());
    }

    private void afficherPaiementsAbonnement() {
        System.out.print("ID de l'abonnement : ");
        String idAbonnement = scanner.nextLine();
        
        List<Paiement> paiements = paiementService.getDerniersPaiements(idAbonnement, Integer.MAX_VALUE);
        if (paiements.isEmpty()) {
            System.out.println("Aucun paiement trouvé");
            return;
        }
        
        System.out.println("\nHistorique des paiements :");
        paiements.forEach(p -> System.out.printf(
            "Date: %s, Statut: %s%n",
            p.getDatePaiement().format(dateFormat),
            p.getStatut()
        ));
    }

    private void afficherDerniersPaiements() {
        System.out.print("ID de l'abonnement : ");
        String idAbonnement = scanner.nextLine();
        
        List<Paiement> paiements = paiementService.getDerniersPaiements(idAbonnement, 5);
        if (paiements.isEmpty()) {
            System.out.println("Aucun paiement trouvé");
            return;
        }
        
        System.out.println("\n5 derniers paiements :");
        paiements.forEach(p -> System.out.printf(
            "Date: %s, Statut: %s%n",
            p.getDatePaiement().format(dateFormat),
            p.getStatut()
        ));
    }

    private void genererEcheances() {
        System.out.print("ID de l'abonnement : ");
        String idAbonnement = scanner.nextLine();
        
        System.out.print("Nombre de mois : ");
        int nombreMois = scanner.nextInt();
        
        List<Paiement> echeances = paiementService.genererEcheancesMensuelles(
            idAbonnement, nombreMois);
        
        System.out.println("\nÉchéances générées :");
        echeances.forEach(e -> System.out.printf(
            "Date échéance: %s%n",
            e.getDateEcheance().format(dateFormat)
        ));
    }

    // Méthodes pour les rapports
    private void afficherMontantTotalImpayes() {
        double total = paiementService.getMontantTotalImpayes();
        System.out.printf("Montant total des impayés : %.2f€%n", total);
    }

    private void listerPaiementsImpayes() {
        List<Paiement> impayes = paiementService.getPaiementsImpayes();
        if (impayes.isEmpty()) {
            System.out.println("Aucun paiement impayé");
            return;
        }
        
        System.out.println("\nListe des paiements impayés :");
        impayes.forEach(p -> System.out.printf(
            "ID Abonnement: %s, Date échéance: %s%n",
            p.getIdAbonnement(),
            p.getDateEcheance().format(dateFormat)
        ));
    }

    private void genererRapportMensuel() {
        System.out.print("Mois (1-12) : ");
        int mois = scanner.nextInt();
        
        System.out.print("Année : ");
        int annee = scanner.nextInt();
        
        Map<String, Double> rapport = paiementService.genererRapportMensuel(mois, annee);
        if (rapport.isEmpty()) {
            System.out.println("Aucune donnée pour cette période");
            return;
        }
        
        System.out.printf("\nRapport mensuel - %02d/%d :%n", mois, annee);
        rapport.forEach((id, montant) -> System.out.printf(
            "Abonnement %s : %.2f€%n", id, montant
        ));
    }

    private void afficherTotalPaiements() {
        System.out.print("ID de l'abonnement : ");
        String idAbonnement = scanner.nextLine();
        
        double total = paiementService.calculerTotalPaiements(idAbonnement);
        System.out.printf("Total des paiements : %.2f€%n", total);
    }

    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();
        ui.start();
    }
}