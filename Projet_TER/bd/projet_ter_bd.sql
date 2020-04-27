-- phpMyAdmin SQL Dump
-- version 4.6.6deb5
-- https://www.phpmyadmin.net/
--
-- Client :  localhost:3306
-- Généré le :  Lun 27 Avril 2020 à 14:58
-- Version du serveur :  5.7.29-0ubuntu0.18.04.1
-- Version de PHP :  7.2.24-0ubuntu0.18.04.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `projet_ter`
--

-- --------------------------------------------------------

--
-- Structure de la table `tc_type_personne`
--

CREATE TABLE `tc_type_personne` (
  `code_type_personne` varchar(2) NOT NULL,
  `libelle_type_personne` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `td_lien`
--

CREATE TABLE `td_lien` (
  `id_lien` int(11) NOT NULL,
  `nom_lien` varchar(200) NOT NULL,
  `contenu_lien` varchar(500) NOT NULL,
  `id_mail` varchar(1000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `td_mail`
--

CREATE TABLE `td_mail` (
  `id_mail` varchar(1000) NOT NULL,
  `date_envoi_mail` varchar(500) NOT NULL,
  `contenu_mail` varchar(15000) NOT NULL,
  `sujet_mail` varchar(500) NOT NULL,
  `email_personne` varchar(200) NOT NULL,
  `id_mail_pere` varchar(1000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `td_mail_destinataire`
--

CREATE TABLE `td_mail_destinataire` (
  `id_mail` varchar(200) NOT NULL,
  `email_personne` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `td_personne`
--

CREATE TABLE `td_personne` (
  `email_personne` varchar(200) NOT NULL,
  `nom_personne` varchar(100) NOT NULL,
  `prenom_personne` varchar(130) NOT NULL,
  `role_personne` varchar(150) NOT NULL,
  `code_type_personne` varchar(2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `td_piece_jointe`
--

CREATE TABLE `td_piece_jointe` (
  `id_piece_jointe` int(11) NOT NULL,
  `nom_piece_jointe` varchar(200) NOT NULL,
  `contenu_piece_jointe` varchar(200) NOT NULL,
  `id_mail` varchar(1000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Index pour les tables exportées
--

--
-- Index pour la table `tc_type_personne`
--
ALTER TABLE `tc_type_personne`
  ADD PRIMARY KEY (`code_type_personne`);

--
-- Index pour la table `td_lien`
--
ALTER TABLE `td_lien`
  ADD PRIMARY KEY (`id_lien`);

--
-- Index pour la table `td_mail`
--
ALTER TABLE `td_mail`
  ADD PRIMARY KEY (`id_mail`);

--
-- Index pour la table `td_mail_destinataire`
--
ALTER TABLE `td_mail_destinataire`
  ADD PRIMARY KEY (`id_mail`,`email_personne`);

--
-- Index pour la table `td_personne`
--
ALTER TABLE `td_personne`
  ADD PRIMARY KEY (`email_personne`);

--
-- Index pour la table `td_piece_jointe`
--
ALTER TABLE `td_piece_jointe`
  ADD PRIMARY KEY (`id_piece_jointe`);

--
-- AUTO_INCREMENT pour les tables exportées
--

--
-- AUTO_INCREMENT pour la table `td_lien`
--
ALTER TABLE `td_lien`
  MODIFY `id_lien` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `td_piece_jointe`
--
ALTER TABLE `td_piece_jointe`
  MODIFY `id_piece_jointe` int(11) NOT NULL AUTO_INCREMENT;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
