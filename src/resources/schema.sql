-- Création de la base de données
CREATE DATABASE IF NOT EXISTS subscription_manager;
USE subscription_manager;

-- Table des abonnements
CREATE TABLE IF NOT EXISTS abonnements (
    id INT AUTO_INCREMENT PRIMARY KEY,
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL,
    montant DECIMAL(10,2) NOT NULL,
    statut ENUM('ACTIF', 'SUSPENDU', 'RESILIE', 'EXPIRE') NOT NULL,
    type ENUM('AVEC_ENGAGEMENT', 'SANS_ENGAGEMENT') NOT NULL,
    duree_engagement INT,
    frais_resiliation DECIMAL(10,2)
);

-- Table des paiements
CREATE TABLE IF NOT EXISTS paiements (
    id INT AUTO_INCREMENT PRIMARY KEY,
    abonnement_id INT NOT NULL,
    montant DECIMAL(10,2) NOT NULL,
    date_paiement DATE NOT NULL,
    statut ENUM('EN_ATTENTE', 'VALIDE', 'REJETE', 'REMBOURSE') NOT NULL,
    FOREIGN KEY (abonnement_id) REFERENCES abonnements(id)
);