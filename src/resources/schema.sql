-- Création des types ENUM
CREATE TYPE statut_abonnement AS ENUM ('ACTIF', 'SUSPENDU', 'RESILIE');
CREATE TYPE type_abonnement AS ENUM ('AVEC_ENGAGEMENT', 'SANS_ENGAGEMENT');
CREATE TYPE statut_paiement AS ENUM ('EN_ATTENTE', 'VALIDE', 'REJETE');

-- Table des abonnements
CREATE TABLE IF NOT EXISTS abonnement (
    id VARCHAR(36) PRIMARY KEY,
    nom_service VARCHAR(100) NOT NULL,
    montant_mensuel DECIMAL(10,2) NOT NULL,
    date_debut DATE NOT NULL,
    date_fin DATE,
    statut statut_abonnement NOT NULL DEFAULT 'ACTIF',
    type type_abonnement NOT NULL,
    duree_engagement INTEGER
);

-- Table des paiements
CREATE TABLE IF NOT EXISTS paiement (
    id_paiement VARCHAR(36) PRIMARY KEY,
    id_abonnement VARCHAR(36) NOT NULL,
    date_echeance DATE NOT NULL,
    date_paiement DATE,
    type_paiement VARCHAR(50),
    statut statut_paiement NOT NULL DEFAULT 'EN_ATTENTE',
    FOREIGN KEY (id_abonnement) REFERENCES abonnement(id)
);

-- Index pour améliorer les performances des requêtes fréquentes
CREATE INDEX idx_abonnement_statut ON abonnement(statut);
CREATE INDEX idx_paiement_date_echeance ON paiement(date_echeance);
CREATE INDEX idx_paiement_statut ON paiement(statut);