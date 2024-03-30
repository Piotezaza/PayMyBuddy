# Désactivation la vérification des contraintes
SET FOREIGN_KEY_CHECKS = 0;

# Suppression des tables déjà existantes
DROP TABLE IF EXISTS `Utilisateur`;
DROP TABLE IF EXISTS `Contact`;
DROP TABLE IF EXISTS `Compte_PMB`;
DROP TABLE IF EXISTS `Compte_bancaire`;
DROP TABLE IF EXISTS `Transaction`;
DROP TABLE IF EXISTS `Virement`;

# Création des différentes tables
CREATE TABLE `Utilisateur`(
    `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `prenom` VARCHAR(255) NOT NULL,
    `nom` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `mot_de_passe` VARCHAR(255) NOT NULL
);

CREATE TABLE `Contact`(
    `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user` INT NOT NULL,
    `contact` INT NOT NULL
);

CREATE TABLE `Compte_PMB`(
    `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY#,
#     `id_utilisateur` INT NOT NULL
);

CREATE TABLE `Compte_bancaire`(
    `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
#     `id_utilisateur` INT NOT NULL,
    `iban` VARCHAR(255) NOT NULL
);

CREATE TABLE `Transaction`(
    `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `date` DATETIME NOT NULL,
    `motif` VARCHAR(255) NOT NULL,
    `montant` DOUBLE NOT NULL,
    `compte_emetteur` INT NOT NULL,
    `compte_receveur` INT NOT NULL
);

CREATE TABLE `Virement`(
    `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `date` DATETIME NOT NULL,
    `type` VARCHAR(255) NOT NULL,
    `montant` INT NOT NULL,
    `id_compte_PMB` INT NOT NULL,
    `id_compte_bancaire` INT NOT NULL
);

# Création des contraintes
ALTER TABLE `Utilisateur` ADD UNIQUE `utilisateur_email_unique`(`email`);
ALTER TABLE `Contact` ADD CONSTRAINT `contact_contact_foreign` FOREIGN KEY(`contact`) REFERENCES `Utilisateur`(`id`);
ALTER TABLE `Contact` ADD CONSTRAINT `contact_utilisateur_foreign` FOREIGN KEY(`user`) REFERENCES `Utilisateur`(`id`);
# ALTER TABLE `Compte_PMB` ADD CONSTRAINT `compte_pmb_id_utilisateur_foreign` FOREIGN KEY(`id_utilisateur`) REFERENCES `Utilisateur`(`id`);
# ALTER TABLE `Compte_bancaire` ADD CONSTRAINT `compte_bancaire_id_utilisateur_foreign` FOREIGN KEY(`id_utilisateur`) REFERENCES `Utilisateur`(`id`);
ALTER TABLE `Compte_bancaire` ADD UNIQUE `compte_bancaire_iban_unique`(`iban`);
ALTER TABLE `Transaction` ADD CONSTRAINT `transaction_compte_emetteur_foreign` FOREIGN KEY(`compte_emetteur`) REFERENCES `Compte_PMB`(`id`);
ALTER TABLE `Transaction` ADD CONSTRAINT `transaction_compte_receveur_foreign` FOREIGN KEY(`compte_receveur`) REFERENCES `Compte_PMB`(`id`);
ALTER TABLE `Virement` ADD CONSTRAINT `virement_id_compte_bancaire_foreign` FOREIGN KEY(`id_compte_bancaire`) REFERENCES `Compte_bancaire`(`id`);
ALTER TABLE `Virement` ADD CONSTRAINT `virement_id_compte_pmb_foreign` FOREIGN KEY(`id_compte_PMB`) REFERENCES `Compte_PMB`(`id`);

# Réactivation la vérification des contraintes
SET FOREIGN_KEY_CHECKS = 1;

# Insertion de données fictives
# INSERT INTO `Utilisateur` (prenom) VALUES ('John'), ('Doe'), ('Wong'), ('Trevor'), ('Peter'), ('Priya'), ('Jane'), ('Salome');
# INSERT INTO `Contact` (user, contact) VALUES (1,2), (1,3), (2,3), (3,5), (6,3);
# INSERT INTO `Compte_PMB` (id_utilisateur) VALUES (1), (2), (3), (4), (5), (6), (7), (8);
# INSERT INTO `Compte_bancaire` (id_utilisateur, iban) VALUES (1, 'FR5710096000303833493877S94'), (2, 'FR3430003000503525919712F95'), (3, 'FR5214508000309922468932O37'), (4, 'FR6317569000306842218958U03'), (5, 'FR1017569000506283175366S97'), (6, 'FR5312739000301562456734F36');