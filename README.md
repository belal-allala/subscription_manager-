# Gestionnaire d'Abonnements

Application Java pour la gestion complète des abonnements et de leurs paiements. Cette application permet de gérer différents types d'abonnements (avec ou sans engagement), suivre les paiements, générer des rapports financiers et plus encore.

## Fonctionnalités

### Gestion des Abonnements
- Création d'abonnements avec ou sans période d'engagement
- Modification et résiliation d'abonnements
- Suspension et réactivation d'abonnements
- Consultation des abonnements actifs

### Gestion des Paiements
- Enregistrement des paiements
- Génération automatique des échéances
- Suivi des paiements par abonnement
- Historique des 5 derniers paiements

### Rapports et Statistiques
- Détection des impayés avec montant total
- Génération de rapports mensuels
- Calcul des montants totaux par abonnement
- Suivi des échéances de paiement

## Technologies Utilisées

- Java 8+ (Programmation orientée objet, Stream API, Lambda expressions)
- PostgreSQL (Base de données relationnelle)
- JDBC (Connexion à la base de données)
- Architecture en couches (DAO, Service, UI)

## Prérequis

- JDK 8 ou supérieur
- PostgreSQL 12 ou supérieur
- Maven (optionnel, pour la gestion des dépendances)

## Installation

1. Cloner le repository :
```bash
git clone https://github.com/belal-allala/subscription_manager-.git
cd subscription_manager
```

2. Configurer la base de données :
- Créer une base de données PostgreSQL nommée 'abonnement'
- Modifier les paramètres de connexion dans `src/main/util/DatabaseManager.java` :
```java
private static final String URL = "jdbc:postgresql://localhost:5432/abonnement";
private static final String USER = "votre_utilisateur";
private static final String PASSWORD = "votre_mot_de_passe";
```

3. Initialiser la base de données :
- Exécuter le script de schéma : `src/resources/schema.sql`
- (Optionnel) Exécuter le script de données de test : `src/resources/init_db.sql`

## Structure du Projet

```
src/
├── main/
│   ├── dao/                  # Couche d'accès aux données
│   │   ├── AbonnementDAO.java
│   │   └── PaiementDAO.java
│   ├── entity/              # Modèles de données
│   │   ├── Abonnement.java
│   │   ├── AbonnementAvecEngagement.java
│   │   ├── AbonnementSansEngagement.java
│   │   ├── Paiement.java
│   │   ├── StatutAbonnement.java
│   │   └── StatutPaiement.java
│   ├── service/            # Logique métier
│   │   ├── AbonnementService.java
│   │   └── PaiementService.java
│   ├── ui/                # Interface utilisateur
│   │   └── ConsoleUI.java
│   └── util/              # Utilitaires
│       ├── DatabaseManager.java
│       └── IdGenerator.java
├── META-INF/              # Configuration
│   └── MANIFEST.MF
└── resources/            # Ressources
    ├── schema.sql
    └── init_db.sql
```

## Utilisation

1. Compiler et exécuter l'application :
```bash
javac -cp "lib/*" src/main/ui/ConsoleUI.java
java -cp "lib/*:src" main.ui.ConsoleUI
```

2. Menu Principal :
   - Gestion des Abonnements
   - Gestion des Paiements
   - Rapports et Statistiques

### Exemples d'Utilisation

#### Créer un Abonnement
1. Sélectionner "Gestion des Abonnements"
2. Choisir le type d'abonnement (avec/sans engagement)
3. Saisir les informations demandées :
   - Nom du service
   - Montant mensuel
   - Date de début
   - Durée d'engagement (si applicable)

#### Enregistrer un Paiement
1. Sélectionner "Gestion des Paiements"
2. Choisir "Enregistrer un paiement"
3. Saisir :
   - ID de l'abonnement
   - Date du paiement
   - Type de paiement

## Contribuer

1. Fork le projet
2. Créer une branche pour votre fonctionnalité
3. Commiter vos changements
4. Pousser vers la branche
5. Ouvrir une Pull Request

## Licence

Ce projet est sous licence MIT. Voir le fichier `LICENSE` pour plus de détails.

## Support

Pour toute question ou problème :
1. Ouvrir une issue sur GitHub
2. Contacter l'équipe de développement