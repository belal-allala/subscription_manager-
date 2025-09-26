-- Insertion de données de test
USE subscription_manager;

-- Insertion d'abonnements de test
INSERT INTO abonnements (date_debut, date_fin, montant, statut, type, duree_engagement, frais_resiliation)
VALUES
    ('2025-01-01', '2026-01-01', 29.99, 'ACTIF', 'AVEC_ENGAGEMENT', 12, NULL),
    ('2025-01-01', '2025-02-01', 19.99, 'ACTIF', 'SANS_ENGAGEMENT', NULL, 15.00);

-- Insertion de paiements de test
INSERT INTO paiements (abonnement_id, montant, date_paiement, statut)
VALUES
    (1, 29.99, '2025-01-01', 'VALIDE'),
    (2, 19.99, '2025-01-01', 'VALIDE');