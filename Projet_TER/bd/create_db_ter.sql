-- phpMyAdmin SQL Dump
-- version 4.6.6deb5
-- https://www.phpmyadmin.net/
--
-- Client :  localhost:3306
-- Généré le :  Dim 03 Mai 2020 à 18:03
-- Version du serveur :  5.7.29-0ubuntu0.18.04.1
-- Version de PHP :  7.2.24-0ubuntu0.18.04.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `ter`
--

-- --------------------------------------------------------

--
-- Structure de la table `tc_fonction`
--

CREATE TABLE `tc_fonction` (
  `code_fonction` varchar(15) NOT NULL,
  `libelle_fonction` varchar(45) DEFAULT NULL,
  `email_email` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `tc_pays`
--

CREATE TABLE `tc_pays` (
  `nom_pays` varchar(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `tc_type_piece_jointe`
--

CREATE TABLE `tc_type_piece_jointe` (
  `code_type_piece_jointe` varchar(2) NOT NULL,
  `libelle_type_piece_jointe` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `tc_ville`
--

CREATE TABLE `tc_ville` (
  `code_postal` int(11) NOT NULL,
  `nom_ville` varchar(150) NOT NULL,
  `nom_pays` varchar(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `td_destinataire`
--

CREATE TABLE `td_destinataire` (
  `id_mail` varchar(200) NOT NULL,
  `email_email` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `td_email`
--

CREATE TABLE `td_email` (
  `email_email` varchar(200) NOT NULL,
  `signature_email` varchar(5000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `td_expediteur`
--

CREATE TABLE `td_expediteur` (
  `id_mail` varchar(200) NOT NULL,
  `email_email` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `td_institution`
--

CREATE TABLE `td_institution` (
  `nom_institution` varchar(45) NOT NULL,
  `code_postal` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `td_lien`
--

CREATE TABLE `td_lien` (
  `id_lien` int(11) NOT NULL,
  `nom_lien` varchar(200) DEFAULT NULL,
  `contenu_lien` varchar(20000) DEFAULT NULL,
  `id_mail` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `td_mail`
--

CREATE TABLE `td_mail` (
  `id_mail` varchar(200) NOT NULL,
  `date_envoi_mail` varchar(200) DEFAULT NULL,
  `sujet_mail` varchar(500) DEFAULT NULL,
  `contenu_mail` longtext,
  `email_email` varchar(200) NOT NULL,
  `id_mail_pere` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `td_personne`
--

CREATE TABLE `td_personne` (
  `id_personne` int(11) NOT NULL,
  `nom_personne` varchar(100) DEFAULT NULL,
  `prenom_personne` varchar(150) DEFAULT NULL,
  `email_email` varchar(200) NOT NULL,
  `nom_institution` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `td_personne_fonction`
--

CREATE TABLE `td_personne_fonction` (
  `id_personne` int(11) NOT NULL,
  `code_fonction` varchar(15) NOT NULL,
  `date_debut_fonction` date DEFAULT NULL,
  `date_fin_fonction` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `td_piece_jointe`
--

CREATE TABLE `td_piece_jointe` (
  `id_piece_jointe` int(11) NOT NULL,
  `nom_piece_jointe` varchar(200) DEFAULT NULL,
  `contenu_piece_jointe` longblob,
  `id_mail` varchar(200) NOT NULL,
  `code_type_piece_jointe` varchar(2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Index pour les tables exportées
--

--
-- Index pour la table `tc_fonction`
--
ALTER TABLE `tc_fonction`
  ADD PRIMARY KEY (`code_fonction`),
  ADD KEY `fk_tc_fonction_td_email1_idx` (`email_email`);

--
-- Index pour la table `tc_pays`
--
ALTER TABLE `tc_pays`
  ADD PRIMARY KEY (`nom_pays`);

--
-- Index pour la table `tc_type_piece_jointe`
--
ALTER TABLE `tc_type_piece_jointe`
  ADD PRIMARY KEY (`code_type_piece_jointe`);

--
-- Index pour la table `tc_ville`
--
ALTER TABLE `tc_ville`
  ADD PRIMARY KEY (`code_postal`),
  ADD KEY `fk_tc_ville_tc_pays1_idx` (`nom_pays`);

--
-- Index pour la table `td_destinataire`
--
ALTER TABLE `td_destinataire`
  ADD PRIMARY KEY (`id_mail`,`email_email`),
  ADD KEY `fk_td_mail_has_td_email_td_email2_idx` (`email_email`),
  ADD KEY `fk_td_mail_has_td_email_td_mail2_idx` (`id_mail`);

--
-- Index pour la table `td_email`
--
ALTER TABLE `td_email`
  ADD PRIMARY KEY (`email_email`);

--
-- Index pour la table `td_expediteur`
--
ALTER TABLE `td_expediteur`
  ADD PRIMARY KEY (`id_mail`,`email_email`),
  ADD KEY `fk_td_mail_has_td_email_td_email1_idx` (`email_email`),
  ADD KEY `fk_td_mail_has_td_email_td_mail1_idx` (`id_mail`);

--
-- Index pour la table `td_institution`
--
ALTER TABLE `td_institution`
  ADD PRIMARY KEY (`nom_institution`),
  ADD KEY `fk_td_institution_tc_ville1_idx` (`code_postal`);

--
-- Index pour la table `td_lien`
--
ALTER TABLE `td_lien`
  ADD PRIMARY KEY (`id_lien`),
  ADD KEY `fk_td_lien_td_mail_idx` (`id_mail`);

--
-- Index pour la table `td_mail`
--
ALTER TABLE `td_mail`
  ADD PRIMARY KEY (`id_mail`),
  ADD KEY `fk_td_mail_td_email1_idx` (`email_email`),
  ADD KEY `fk_td_mail_td_mail` (`id_mail_pere`);

--
-- Index pour la table `td_personne`
--
ALTER TABLE `td_personne`
  ADD PRIMARY KEY (`id_personne`),
  ADD KEY `fk_td_personne_td_email1_idx` (`email_email`),
  ADD KEY `fk_td_personne_td_institution1_idx` (`nom_institution`);

--
-- Index pour la table `td_personne_fonction`
--
ALTER TABLE `td_personne_fonction`
  ADD PRIMARY KEY (`id_personne`,`code_fonction`),
  ADD KEY `fk_td_personne_has_tc_fonction_tc_fonction1_idx` (`code_fonction`),
  ADD KEY `fk_td_personne_has_tc_fonction_td_personne1_idx` (`id_personne`);

--
-- Index pour la table `td_piece_jointe`
--
ALTER TABLE `td_piece_jointe`
  ADD PRIMARY KEY (`id_piece_jointe`),
  ADD KEY `fk_td_piece_jointe_td_mail1_idx` (`id_mail`),
  ADD KEY `fk_td_piece_jointe_tc_type_piece_jointe1_idx` (`code_type_piece_jointe`);

--
-- AUTO_INCREMENT pour les tables exportées
--

--
-- AUTO_INCREMENT pour la table `td_lien`
--
ALTER TABLE `td_lien`
  MODIFY `id_lien` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `td_personne`
--
ALTER TABLE `td_personne`
  MODIFY `id_personne` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=163;
--
-- AUTO_INCREMENT pour la table `td_piece_jointe`
--
ALTER TABLE `td_piece_jointe`
  MODIFY `id_piece_jointe` int(11) NOT NULL AUTO_INCREMENT;
--
-- Contraintes pour les tables exportées
--

--
-- Contraintes pour la table `tc_fonction`
--
ALTER TABLE `tc_fonction`
  ADD CONSTRAINT `fk_tc_fonction_td_email1` FOREIGN KEY (`email_email`) REFERENCES `td_email` (`email_email`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Contraintes pour la table `tc_ville`
--
ALTER TABLE `tc_ville`
  ADD CONSTRAINT `fk_tc_ville_tc_pays1` FOREIGN KEY (`nom_pays`) REFERENCES `tc_pays` (`nom_pays`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Contraintes pour la table `td_destinataire`
--
ALTER TABLE `td_destinataire`
  ADD CONSTRAINT `fk_td_mail_has_td_email_td_email2` FOREIGN KEY (`email_email`) REFERENCES `td_email` (`email_email`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_td_mail_has_td_email_td_mail2` FOREIGN KEY (`id_mail`) REFERENCES `td_mail` (`id_mail`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Contraintes pour la table `td_expediteur`
--
ALTER TABLE `td_expediteur`
  ADD CONSTRAINT `fk_td_mail_has_td_email_td_email1` FOREIGN KEY (`email_email`) REFERENCES `td_email` (`email_email`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_td_mail_has_td_email_td_mail1` FOREIGN KEY (`id_mail`) REFERENCES `td_mail` (`id_mail`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Contraintes pour la table `td_institution`
--
ALTER TABLE `td_institution`
  ADD CONSTRAINT `fk_td_institution_tc_ville1` FOREIGN KEY (`code_postal`) REFERENCES `tc_ville` (`code_postal`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Contraintes pour la table `td_lien`
--
ALTER TABLE `td_lien`
  ADD CONSTRAINT `fk_td_lien_td_mail` FOREIGN KEY (`id_mail`) REFERENCES `td_mail` (`id_mail`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Contraintes pour la table `td_mail`
--
ALTER TABLE `td_mail`
  ADD CONSTRAINT `fk_td_mail_td_email1` FOREIGN KEY (`email_email`) REFERENCES `td_email` (`email_email`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_td_mail_td_mail` FOREIGN KEY (`id_mail_pere`) REFERENCES `td_mail` (`id_mail`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Contraintes pour la table `td_personne`
--
ALTER TABLE `td_personne`
  ADD CONSTRAINT `fk_td_personne_td_email1` FOREIGN KEY (`email_email`) REFERENCES `td_email` (`email_email`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_td_personne_td_institution` FOREIGN KEY (`nom_institution`) REFERENCES `td_institution` (`nom_institution`);

--
-- Contraintes pour la table `td_personne_fonction`
--
ALTER TABLE `td_personne_fonction`
  ADD CONSTRAINT `fk_personne_fonction` FOREIGN KEY (`id_personne`) REFERENCES `td_personne` (`id_personne`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_td_personne_has_tc_fonction_tc_fonction1` FOREIGN KEY (`code_fonction`) REFERENCES `tc_fonction` (`code_fonction`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Contraintes pour la table `td_piece_jointe`
--
ALTER TABLE `td_piece_jointe`
  ADD CONSTRAINT `fk_td_piece_jointe_tc_type_piece_jointe1` FOREIGN KEY (`code_type_piece_jointe`) REFERENCES `tc_type_piece_jointe` (`code_type_piece_jointe`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_td_piece_jointe_td_mail1` FOREIGN KEY (`id_mail`) REFERENCES `td_mail` (`id_mail`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
