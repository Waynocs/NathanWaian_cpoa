-- phpMyAdmin SQL Dump
-- version 4.4.15.10
-- https://www.phpmyadmin.net
--
-- Client :  devbdd.iutmetz.univ-lorraine.fr
-- Généré le :  Ven 02 Octobre 2020 à 08:15
-- Version du serveur :  10.3.24-MariaDB
-- Version de PHP :  7.1.33

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `serano1u_ToutDur`
--

-- --------------------------------------------------------

--
-- Structure de la table `Categorie`
--

CREATE TABLE IF NOT EXISTS `Categorie` (
  `id_categorie` int(3) NOT NULL,
  `titre` varchar(30) NOT NULL,
  `visuel` varchar(30) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

--
-- Contenu de la table `Categorie`
--

INSERT INTO `Categorie` (`id_categorie`, `titre`, `visuel`) VALUES
(1, 'Pulls', 'lespulls.png'),
(2, 'Bonnets', 'lesbonnets.png'),
(4, 'chaussettes', 'leschaussettes.png');

-- --------------------------------------------------------

--
-- Structure de la table `Client`
--

CREATE TABLE IF NOT EXISTS `Client` (
  `id_client` int(5) NOT NULL,
  `nom` varchar(25) NOT NULL,
  `prenom` varchar(30) NOT NULL,
  `identifiant` varchar(45) NOT NULL,
  `mot_de_passe` varchar(16) NOT NULL,
  `adr_numero` varchar(5) NOT NULL,
  `adr_voie` varchar(30) NOT NULL,
  `adr_code_postal` varchar(10) NOT NULL,
  `adr_ville` varchar(20) NOT NULL,
  `adr_pays` varchar(20) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Contenu de la table `Client`
--

INSERT INTO `Client` (`id_client`, `nom`, `prenom`, `identifiant`, `mot_de_passe`, `adr_numero`, `adr_voie`, `adr_code_postal`, `adr_ville`, `adr_pays`) VALUES
(1, 'LAROCHE', 'Pierre', 'pl@ul.fr', 'toto', '12', 'rue des étudiants', '57990', 'Metz', 'France');

-- --------------------------------------------------------

--
-- Structure de la table `Commande`
--

CREATE TABLE IF NOT EXISTS `Commande` (
  `id_commande` int(7) NOT NULL,
  `date_commande` datetime NOT NULL,
  `id_client` int(5) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

--
-- Contenu de la table `Commande`
--

INSERT INTO `Commande` (`id_commande`, `date_commande`, `id_client`) VALUES
(1, '2020-09-02 13:12:00', 1),
(2, '2020-08-30 11:22:00', 1);

-- --------------------------------------------------------

--
-- Structure de la table `Ligne_commande`
--

CREATE TABLE IF NOT EXISTS `Ligne_commande` (
  `id_commande` int(7) NOT NULL,
  `id_produit` int(5) NOT NULL,
  `quantite` int(3) NOT NULL,
  `tarif_unitaire` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Contenu de la table `Ligne_commande`
--

INSERT INTO `Ligne_commande` (`id_commande`, `id_produit`, `quantite`, `tarif_unitaire`) VALUES
(1, 2, 2, 41.5),
(1, 6, 1, 15),
(2, 12, 4, 35);

-- --------------------------------------------------------

--
-- Structure de la table `Produit`
--

CREATE TABLE IF NOT EXISTS `Produit` (
  `id_produit` int(5) NOT NULL,
  `nom` varchar(50) NOT NULL,
  `description` varchar(500) NOT NULL,
  `tarif` float NOT NULL,
  `visuel` varchar(30) NOT NULL,
  `id_categorie` int(3) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1;

--
-- Contenu de la table `Produit`
--

INSERT INTO `Produit` (`id_produit`, `nom`, `description`, `tarif`, `visuel`, `id_categorie`) VALUES
(2, 'Sonic te kiffe', 'Inspiré par la saga Séga (c''est plus fort que toi !), un pull 100% gamer qui te permettra de faire baver d''envie tes petits camarades de jeu.', 41.5, 'pull1.png', 1),
(6, 'La chaleur des rennes', 'Classique mais efficace, un bonnet dont l''élégance n''est pas à souligner, il vous grattera comme il faut !', 15, 'bonnet0.png', 2),
(12, 'Dall', 'Joyeux Noël avec nos petits lutins dansants !', 35, 'bonnet1.png', 2);

--
-- Index pour les tables exportées
--

--
-- Index pour la table `Categorie`
--
ALTER TABLE `Categorie`
  ADD PRIMARY KEY (`id_categorie`);

--
-- Index pour la table `Client`
--
ALTER TABLE `Client`
  ADD PRIMARY KEY (`id_client`);

--
-- Index pour la table `Commande`
--
ALTER TABLE `Commande`
  ADD PRIMARY KEY (`id_commande`),
  ADD KEY `id_client` (`id_client`);

--
-- Index pour la table `Ligne_commande`
--
ALTER TABLE `Ligne_commande`
  ADD PRIMARY KEY (`id_commande`,`id_produit`),
  ADD KEY `id_produit` (`id_produit`);

--
-- Index pour la table `Produit`
--
ALTER TABLE `Produit`
  ADD PRIMARY KEY (`id_produit`),
  ADD KEY `id_categorie` (`id_categorie`);

--
-- AUTO_INCREMENT pour les tables exportées
--

--
-- AUTO_INCREMENT pour la table `Categorie`
--
ALTER TABLE `Categorie`
  MODIFY `id_categorie` int(3) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT pour la table `Client`
--
ALTER TABLE `Client`
  MODIFY `id_client` int(5) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT pour la table `Commande`
--
ALTER TABLE `Commande`
  MODIFY `id_commande` int(7) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=8;
--
-- AUTO_INCREMENT pour la table `Produit`
--
ALTER TABLE `Produit`
  MODIFY `id_produit` int(5) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=15;
--
-- Contraintes pour les tables exportées
--

--
-- Contraintes pour la table `Commande`
--
ALTER TABLE `Commande`
  ADD CONSTRAINT `Commande_ibfk_1` FOREIGN KEY (`id_client`) REFERENCES `Client` (`id_client`);

--
-- Contraintes pour la table `Ligne_commande`
--
ALTER TABLE `Ligne_commande`
  ADD CONSTRAINT `Ligne_commande_ibfk_1` FOREIGN KEY (`id_produit`) REFERENCES `Produit` (`id_produit`),
  ADD CONSTRAINT `Ligne_commande_ibfk_2` FOREIGN KEY (`id_commande`) REFERENCES `Commande` (`id_commande`);

--
-- Contraintes pour la table `Produit`
--
ALTER TABLE `Produit`
  ADD CONSTRAINT `Produit_ibfk_1` FOREIGN KEY (`id_categorie`) REFERENCES `Categorie` (`id_categorie`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
