package main.ui;

import main.service.AbonnementService;
import main.service.PaiementService;
import java.util.Scanner;

public class ConsoleUI {
    private final AbonnementService abonnementService;
    private final PaiementService paiementService;
    private final Scanner scanner;

    public ConsoleUI() {
        this.abonnementService = new AbonnementService();
        this.paiementService = new PaiementService();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        // TODO: Implement console menu and user interaction
    }

    // Helper methods for user interaction
    private void afficherMenu() {
        // TODO: Implement menu display
    }

    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();
        ui.start();
    }
}