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

    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String BLUE = "\u001B[34m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";
    private static final String MAGENTA = "\u001B[35m";
    private static final String BOLD = "\u001B[1m";

    public ConsoleUI() {
        this.abonnementService = new AbonnementService();
        this.paiementService = new PaiementService();
        this.scanner = new Scanner(System.in);
        this.dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.running = true;
    }

    private void afficherBanniere() {
        System.out.println(CYAN + BOLD);
        System.out.println("╔═══════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                                       ║");
        System.out.println("║     ███████╗██╗   ██╗██████╗ ███████╗ ██████╗ ██╗   ██╗██████╗      ║");
        System.out.println("║     ██╔════╝██║   ██║██╔══██╗██╔════╝██╔═══██╗██║   ██║██╔══██╗     ║");
        System.out.println("║     ███████╗██║   ██║██████╔╝███████╗██║   ██║██║   ██║██████╔╝     ║");
        System.out.println("║     ╚════██║██║   ██║██╔══██╗╚════██║██║   ██║██║   ██║██╔══██╗     ║");
        System.out.println("║     ███████║╚██████╔╝██████╔╝███████║╚██████╔╝╚██████╔╝██████╔╝     ║");
        System.out.println("║     ╚══════╝ ╚═════╝ ╚═════╝ ╚══════╝ ╚═════╝  ╚═════╝ ╚═════╝      ║");
        System.out.println("║                                                                       ║");
        System.out.println("║              " + YELLOW + "Gestionnaire d'Abonnements " + CYAN + "                    ║");
        System.out.println("║                    " + GREEN + "Belal ALLALA" + CYAN + "                        ║");
        System.out.println("║                                                                       ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════════╝");
        System.out.println(RESET);
    }

    private void afficherSeparateur() {
        System.out.println(BLUE + "═══════════════════════════════════════════════════════════════════════" + RESET);
    }

    private void afficherLigneMenu(String numero, String texte, String icone) {
        System.out.println(CYAN + "║ " + YELLOW + icone + " " + BOLD + numero + RESET + ". " + texte);
    }

    public void start() {
        afficherBanniere();
        System.out.println(GREEN + BOLD + " Bienvenue dans le gestionnaire d'abonnements " + RESET);
        System.out.println();
        
        while (running) {
            afficherMenu();
            int choix = lireChoix();
            traiterChoix(choix);
        }
    }

    private void afficherMenu() {
        System.out.println();
        afficherSeparateur();
        System.out.println(CYAN + BOLD + "╔═══════════════════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(CYAN + BOLD + "║                         MENU PRINCIPAL                                ║" + RESET);
        System.out.println(CYAN + BOLD + "╚═══════════════════════════════════════════════════════════════════════╝" + RESET);
        afficherLigneMenu("1", "Gestion des Abonnements", "");
        afficherLigneMenu("2", "Gestion des Paiements", "");
        afficherLigneMenu("3", "Rapports et Statistiques", "");
        afficherLigneMenu("0", "Quitter", "");
        afficherSeparateur();
        System.out.print(BOLD + "=> Votre choix : " + RESET);
    }

    private void afficherMenuAbonnements() {
        System.out.println();
        afficherSeparateur();
        System.out.println(MAGENTA + BOLD + "╔═══════════════════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(MAGENTA + BOLD + "║                    GESTION DES ABONNEMENTS                            ║" + RESET);
        System.out.println(MAGENTA + BOLD + "╚═══════════════════════════════════════════════════════════════════════╝" + RESET);
        afficherLigneMenu("1", "Créer un abonnement avec engagement", "");
        afficherLigneMenu("2", "Créer un abonnement sans engagement", "");
        afficherLigneMenu("3", "Modifier un abonnement", "");
        afficherLigneMenu("4", "Résilier un abonnement", "");
        afficherLigneMenu("5", "Suspendre un abonnement", "");
        afficherLigneMenu("6", "Réactiver un abonnement", "");
        afficherLigneMenu("7", "Liste des abonnements actifs", "");
        afficherLigneMenu("0", "Retour", "");
        afficherSeparateur();
        System.out.print(BOLD + "=> Votre choix : " + RESET);
    }

    private void afficherMenuPaiements() {
        System.out.println();
        afficherSeparateur();
        System.out.println(GREEN + BOLD + "╔═══════════════════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(GREEN + BOLD + "║                     GESTION DES PAIEMENTS                             ║" + RESET);
        System.out.println(GREEN + BOLD + "╚═══════════════════════════════════════════════════════════════════════╝" + RESET);
        afficherLigneMenu("1", "Enregistrer un paiement", "");
        afficherLigneMenu("2", "Afficher les paiements d'un abonnement", "");
        afficherLigneMenu("3", "Afficher les 5 derniers paiements", "");
        afficherLigneMenu("4", "Générer les échéances mensuelles", "");
        afficherLigneMenu("0", "Retour", "");
        afficherSeparateur();
        System.out.print(BOLD + "=> Votre choix : " + RESET);
    }

    private void afficherMenuRapports() {
        System.out.println();
        afficherSeparateur();
        System.out.println(YELLOW + BOLD + "╔═══════════════════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(YELLOW + BOLD + "║                   RAPPORTS ET STATISTIQUES                            ║" + RESET);
        System.out.println(YELLOW + BOLD + "╚═══════════════════════════════════════════════════════════════════════╝" + RESET);
        afficherLigneMenu("1", "Montant total des impayés", "");
        afficherLigneMenu("2", "Liste des paiements impayés", "");
        afficherLigneMenu("3", "Rapport mensuel", "");
        afficherLigneMenu("4", "Total payé pour un abonnement", "");
        afficherLigneMenu("0", "Retour", "");
        afficherSeparateur();
        System.out.print(BOLD + "=> Votre choix : " + RESET);
    }

    private int lireChoix() {
        try {
            String input = scanner.nextLine().trim();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println(RED + " Veuillez entrer un nombre valide." + RESET);
            return -1;
        }
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
                System.out.println();
                System.out.println(CYAN + "╔═══════════════════════════════════════════════════════════════════════╗" + RESET);
                System.out.println(CYAN + "║                     Merci et à bientôt!                           ║" + RESET);
                System.out.println(CYAN + "╚═══════════════════════════════════════════════════════════════════════╝" + RESET);
                break;
            default:
                System.out.println(RED + " Choix invalide" + RESET);
        }
    }

    private void gererAbonnements() {
        afficherMenuAbonnements();
        int choix = lireChoix();

        try {
            switch (choix) {
                case 1:
                    creerAbonnementAvecEngagement();
                    break;
                case 2:
                    creerAbonnementSansEngagement();
                    break;
                case 3:
                    modifierAbonnement();
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
                    System.out.println(RED + " Choix invalide" + RESET);
            }
        } catch (Exception e) {
            System.out.println(RED + " Erreur : " + e.getMessage() + RESET);
        }
    }

    private void gererPaiements() {
        afficherMenuPaiements();
        int choix = lireChoix();

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
                    System.out.println(RED + " Choix invalide" + RESET);
            }
        } catch (Exception e) {
            System.out.println(RED + " Erreur : " + e.getMessage() + RESET);
        }
    }

    private void gererRapports() {
        afficherMenuRapports();
        int choix = lireChoix();

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
                    System.out.println(RED + " Choix invalide" + RESET);
            }
        } catch (Exception e) {
            System.out.println(RED + " Erreur : " + e.getMessage() + RESET);
        }
    }

    // Méthodes pour les abonnements
    private void creerAbonnementAvecEngagement() {
        System.out.println(CYAN + "\n Creation d un abonnement avec engagement" + RESET);
        System.out.print("Nom du service : ");
        String nomService = scanner.nextLine();
        
        System.out.print("Montant mensuel : ");
        double montant = scanner.nextDouble();
        scanner.nextLine();
        
        System.out.print("Date de début (dd/MM/yyyy) : ");
        LocalDate dateDebut = LocalDate.parse(scanner.nextLine(), dateFormat);
        
        System.out.print("Durée d'engagement (en mois) : ");
        int dureeEngagement = scanner.nextInt();
        scanner.nextLine();
        
        Abonnement abonnement = abonnementService.creerAbonnementAvecEngagement(
            nomService, montant, dateDebut, dureeEngagement);
        
        System.out.println(GREEN + " Abonnement cree avec succes. ID : " + abonnement.getId() + RESET);
    }

    private void creerAbonnementSansEngagement() {
        System.out.println(CYAN + "\n Creation d un abonnement sans engagement" + RESET);
        System.out.print("Nom du service : ");
        String nomService = scanner.nextLine();
        
        System.out.print("Montant mensuel : ");
        double montant = scanner.nextDouble();
        scanner.nextLine();
        
        System.out.print("Date de début (dd/MM/yyyy) : ");
        LocalDate dateDebut = LocalDate.parse(scanner.nextLine(), dateFormat);
        
        Abonnement abonnement = abonnementService.creerAbonnementSansEngagement(
            nomService, montant, dateDebut);
        
        System.out.println(GREEN + " Abonnement cree avec succes. ID : " + abonnement.getId() + RESET);
    }

    private void modifierAbonnement() {
        System.out.println(CYAN + "\n Modification d un abonnement" + RESET);
        System.out.print("ID de l abonnement : ");
        String id = scanner.nextLine();
        
        List<Abonnement> abonnements = abonnementService.listerTousAbonnements();
        Abonnement abonnementExistant = null;
        for (Abonnement a : abonnements) {
            if (a.getId().equals(id)) {
                abonnementExistant = a;
                break;
            }
        }
        
        if (abonnementExistant == null) {
            System.out.println(RED + " Abonnement non trouve" + RESET);
            return;
        }
        
        System.out.println(YELLOW + "\nInformations actuelles :" + RESET);
        System.out.printf("Service: %s, Montant: %.2f€/mois, Statut: %s%n",
            abonnementExistant.getNomService(), 
            abonnementExistant.getMontantMensuel(),
            abonnementExistant.getStatut());
        
        System.out.println(CYAN + "\nQue souhaitez-vous modifier ?" + RESET);
        System.out.println("1. Nom du service");
        System.out.println("2. Montant mensuel");
        System.out.println("3. Les deux");
        System.out.print("Votre choix : ");
        
        int choixModif = lireChoix();
        
        String nouveauNom = abonnementExistant.getNomService();
        double nouveauMontant = abonnementExistant.getMontantMensuel();
        
        if (choixModif == 1 || choixModif == 3) {
            System.out.print("Nouveau nom du service : ");
            nouveauNom = scanner.nextLine();
        }
        
        if (choixModif == 2 || choixModif == 3) {
            System.out.print("Nouveau montant mensuel : ");
            nouveauMontant = scanner.nextDouble();
            scanner.nextLine();
        }
        
        abonnementExistant.setNomService(nouveauNom);
        abonnementExistant.setMontantMensuel(nouveauMontant);
        
        if (abonnementService.modifierAbonnement(abonnementExistant)) {
            System.out.println(GREEN + " Abonnement modifie avec succes" + RESET);
        } else {
            System.out.println(RED + " Erreur lors de la modification" + RESET);
        }
    }

    private void resilierAbonnement() {
        System.out.println(CYAN + "\n Resiliation d un abonnement" + RESET);
        System.out.print("ID de l abonnement : ");
        String id = scanner.nextLine();
        
        System.out.print("Date de resiliation (dd/MM/yyyy) : ");
        LocalDate dateResiliation = LocalDate.parse(scanner.nextLine(), dateFormat);
        
        abonnementService.resilierAbonnement(id, dateResiliation);
        System.out.println(GREEN + " Abonnement resilie avec succes" + RESET);
    }

    private void suspendreAbonnement() {
        System.out.println(CYAN + "\n Suspension d un abonnement" + RESET);
        System.out.print("ID de l abonnement : ");
        String id = scanner.nextLine();
        
        abonnementService.suspendreAbonnement(id);
        System.out.println(GREEN + " Abonnement suspendu avec succes" + RESET);
    }

    private void reactiverAbonnement() {
        System.out.println(CYAN + "\n Reactivation d un abonnement" + RESET);
        System.out.print("ID de l abonnement : ");
        String id = scanner.nextLine();
        
        abonnementService.reactiverAbonnement(id);
        System.out.println(GREEN + " Abonnement reactive avec succes" + RESET);
    }

    private void listerAbonnementsActifs() {
        List<Abonnement> abonnements = abonnementService.listerAbonnementsActifs();
        if (abonnements.isEmpty()) {
            System.out.println(YELLOW + "\n Aucun abonnement actif" + RESET);
            return;
        }
        
        System.out.println(CYAN + "\n Liste des abonnements actifs :" + RESET);
        afficherSeparateur();
        for (Abonnement a : abonnements) {
            System.out.printf(GREEN + "✓ " + RESET + "ID: %s | Service: %s | Montant: %.2f€/mois%n",
                a.getId(), a.getNomService(), a.getMontantMensuel());
        }
        afficherSeparateur();
    }

    // Méthodes pour les paiements
    private void enregistrerPaiement() {
        System.out.println(CYAN + "\n Enregistrement d un paiement" + RESET);
        System.out.print("ID de l abonnement : ");
        String idAbonnement = scanner.nextLine();
        
        System.out.print("Date du paiement (dd/MM/yyyy) : ");
        LocalDate datePaiement = LocalDate.parse(scanner.nextLine(), dateFormat);
        
        System.out.print("Type de paiement : ");
        String typePaiement = scanner.nextLine();
        
        Paiement paiement = paiementService.enregistrerPaiement(
            idAbonnement, datePaiement, typePaiement);
        
        System.out.println(GREEN + " Paiement enregistre avec succes. ID : " + paiement.getIdPaiement() + RESET);
    }

    private void afficherPaiementsAbonnement() {
        System.out.println(CYAN + "\n Historique des paiements" + RESET);
        System.out.print("ID de l abonnement : ");
        String idAbonnement = scanner.nextLine();
        
        List<Paiement> paiements = paiementService.getDerniersPaiements(idAbonnement, Integer.MAX_VALUE);
        if (paiements.isEmpty()) {
            System.out.println(YELLOW + " Aucun paiement trouve" + RESET);
            return;
        }
        
        System.out.println(CYAN + "\nHistorique des paiements :" + RESET);
        afficherSeparateur();
        for (Paiement p : paiements) {
            String couleur = p.getStatut() == StatutPaiement.VALIDE ? GREEN : YELLOW;
            System.out.printf(couleur + "• " + RESET + "Date: %s | Statut: %s%n",
                p.getDatePaiement().format(dateFormat),
                p.getStatut());
        }
        afficherSeparateur();
    }

    private void afficherDerniersPaiements() {
        System.out.println(CYAN + "\n 5 derniers paiements" + RESET);
        System.out.print("ID de l abonnement : ");
        String idAbonnement = scanner.nextLine();
        
        List<Paiement> paiements = paiementService.getDerniersPaiements(idAbonnement, 5);
        if (paiements.isEmpty()) {
            System.out.println(YELLOW + " Aucun paiement trouve" + RESET);
            return;
        }
        
        System.out.println(CYAN + "\n5 derniers paiements :" + RESET);
        afficherSeparateur();
        for (Paiement p : paiements) {
            String couleur = p.getStatut() == StatutPaiement.VALIDE ? GREEN : YELLOW;
            System.out.printf(couleur + "• " + RESET + "Date: %s | Statut: %s%n",
                p.getDatePaiement().format(dateFormat),
                p.getStatut());
        }
        afficherSeparateur();
    }

    private void genererEcheances() {
        System.out.println(CYAN + "\n Generation des echeances" + RESET);
        System.out.print("ID de l abonnement : ");
        String idAbonnement = scanner.nextLine();
        
        System.out.print("Nombre de mois : ");
        int nombreMois = scanner.nextInt();
        scanner.nextLine();
        
        List<Paiement> echeances = paiementService.genererEcheancesMensuelles(
            idAbonnement, nombreMois);
        
        System.out.println(CYAN + "\nEcheances generees :" + RESET);
        afficherSeparateur();
        for (Paiement e : echeances) {
            System.out.printf(GREEN + "✓ " + RESET + "Date echeance: %s%n",
                e.getDateEcheance().format(dateFormat));
        }
        afficherSeparateur();
    }

    // Méthodes pour les rapports
    private void afficherMontantTotalImpayes() {
        double total = paiementService.getMontantTotalImpayes();
        System.out.println();
        afficherSeparateur();
        System.out.printf(RED + " Montant total des impayes : %.2f€%n" + RESET, total);
        afficherSeparateur();
    }

    private void listerPaiementsImpayes() {
        List<Paiement> impayes = paiementService.getPaiementsImpayes();
        if (impayes.isEmpty()) {
            System.out.println(GREEN + "\n Aucun paiement impaye" + RESET);
            return;
        }
        
        System.out.println(RED + "\n Liste des paiements impayes :" + RESET);
        afficherSeparateur();
        for (Paiement p : impayes) {
            System.out.printf(RED +  RESET + "ID Abonnement: %s | Date échéance: %s%n",
                p.getIdAbonnement(),
                p.getDateEcheance().format(dateFormat));
        }
        afficherSeparateur();
    }

    private void genererRapportMensuel() {
        System.out.println(CYAN + "\n Rapport mensuel" + RESET);
        System.out.print("Mois (1-12) : ");
        int mois = scanner.nextInt();
        
        System.out.print("Annee : ");
        int annee = scanner.nextInt();
        scanner.nextLine();
        
        Map<String, Double> rapport = paiementService.genererRapportMensuel(mois, annee);
        if (rapport.isEmpty()) {
            System.out.println(YELLOW + " Aucune donnee pour cette periode" + RESET);
            return;
        }
        
        System.out.printf(CYAN + "\nRapport mensuel - %02d/%d :%n" + RESET, mois, annee);
        afficherSeparateur();
        for (Map.Entry<String, Double> entry : rapport.entrySet()) {
            System.out.printf(GREEN + "• " + RESET + "Abonnement %s : %.2f€%n", 
                entry.getKey(), entry.getValue());
        }
        afficherSeparateur();
    }

    private void afficherTotalPaiements() {
        System.out.println(CYAN + "\n Total des paiements" + RESET);
        System.out.print("ID de l abonnement : ");
        String idAbonnement = scanner.nextLine();
        
        double total = paiementService.calculerTotalPaiements(idAbonnement);
        afficherSeparateur();
        System.out.printf(GREEN + " Total des paiements : %.2f€%n" + RESET, total);
        afficherSeparateur();
    }
    
    // Point d'entrée
    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();
        ui.start();
    }
}
