-- phpMyAdmin SQL Dump
-- version 4.8.0.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le :  sam. 06 juin 2020 à 14:57
-- Version du serveur :  10.1.32-MariaDB
-- Version de PHP :  7.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `api-mobile`
--

-- --------------------------------------------------------

--
-- Structure de la table `activite`
--

CREATE TABLE `activite` (
  `id` bigint(20) NOT NULL,
  `valeur` varchar(100) NOT NULL,
  `idEspace` bigint(20) NOT NULL,
  `idIndicateur` bigint(20) NOT NULL,
  `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `activite`
--

INSERT INTO `activite` (`id`, `valeur`, `idEspace`, `idIndicateur`, `date`) VALUES
(55, '1', 1, 1, '2020-05-24'),
(56, '0', 1, 2, '2020-05-24'),
(57, '0', 2, 3, '2020-06-24'),
(58, '0', 2, 4, '2020-06-24'),
(59, '0', 1, 1, '2020-06-24'),
(60, '0', 1, 2, '2020-06-24');

-- --------------------------------------------------------

--
-- Structure de la table `espace`
--

CREATE TABLE `espace` (
  `id` bigint(20) NOT NULL,
  `nomEspace` varchar(100) DEFAULT NULL,
  `idUser` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `espace`
--

INSERT INTO `espace` (`id`, `nomEspace`, `idUser`) VALUES
(1, 'Velo', 1),
(2, 'Régime', 1),
(3, 'Tabac', 2),
(4, 'Ecole', 2);

--
-- Déclencheurs `espace`
--
DELIMITER $$
CREATE TRIGGER `espace_BEFORE_DELETE` BEFORE DELETE ON `espace` FOR EACH ROW BEGIN
DELETE FROM indicateur WHERE idEspace = OLD.id;
DELETE FROM activite WHERE idEspace = OLD.id;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Structure de la table `indicateur`
--

CREATE TABLE `indicateur` (
  `id` bigint(20) NOT NULL,
  `nomIndicateur` varchar(100) NOT NULL,
  `type` varchar(100) NOT NULL,
  `valeurInit` varchar(100) DEFAULT NULL,
  `idEspace` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `indicateur`
--

INSERT INTO `indicateur` (`id`, `nomIndicateur`, `type`, `valeurInit`, `idEspace`) VALUES
(1, 'Nombre de kilometres', 'number', '0', 1),
(2, 'Durée en minutes', 'number', '0', 1),
(3, 'Nombre de fruits et légumes', 'number', '0', 2),
(4, 'Grammes de protéines', 'number', '0', 2),
(5, 'Nombre de cigarettes fumées', 'number', '0', 3),
(6, 'Paquet acheté', 'boolean', '0', 3),
(7, 'Temps de révision en minutes', 'number', '0', 4),
(8, 'Temps d exercices en minutes', 'number', '0', 4);

--
-- Déclencheurs `indicateur`
--
DELIMITER $$
CREATE TRIGGER `indicateur_BEFORE_DELETE` BEFORE DELETE ON `indicateur` FOR EACH ROW BEGIN
DELETE FROM activite WHERE idIndicateur = OLD.id;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Doublure de structure pour la vue `myview`
-- (Voir ci-dessous la vue réelle)
--
CREATE TABLE `myview` (
`idIndicateur` bigint(20)
,`idEspace` bigint(20)
,`idUser` bigint(20)
,`date` date
,`valeur` varchar(100)
,`nomIndicateur` varchar(100)
,`type` varchar(100)
,`valeurInit` varchar(100)
,`nomEspace` varchar(100)
,`login` varchar(100)
);

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `nom` varchar(100) DEFAULT NULL,
  `prenom` varchar(100) DEFAULT NULL,
  `login` varchar(100) NOT NULL,
  `passwd` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `user`
--

INSERT INTO `user` (`id`, `nom`, `prenom`, `login`, `passwd`) VALUES
(1, 'CHRETIEN', 'Maxence', 'max', 'max'),
(2, 'SABRE', 'Nicolas', 'nico', 'nico');

-- --------------------------------------------------------

--
-- Structure de la vue `myview`
--
DROP TABLE IF EXISTS `myview`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `myview`  AS  select `i`.`id` AS `idIndicateur`,`e`.`id` AS `idEspace`,`u`.`id` AS `idUser`,`a`.`date` AS `date`,`a`.`valeur` AS `valeur`,`i`.`nomIndicateur` AS `nomIndicateur`,`i`.`type` AS `type`,`i`.`valeurInit` AS `valeurInit`,`e`.`nomEspace` AS `nomEspace`,`u`.`login` AS `login` from (((`user` `u` join `espace` `e`) join `indicateur` `i`) join `activite` `a`) where ((`u`.`id` = `e`.`idUser`) and (`e`.`id` = `i`.`idEspace`) and (`e`.`id` = `a`.`idEspace`) and (`i`.`id` = `a`.`idIndicateur`)) ;

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `activite`
--
ALTER TABLE `activite`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `activite_id_uindex` (`id`),
  ADD UNIQUE KEY `un_espace_par_jour_unique_espace_date` (`idEspace`,`idIndicateur`,`date`),
  ADD KEY `activite_idindicateur_id_fk` (`idIndicateur`);

--
-- Index pour la table `espace`
--
ALTER TABLE `espace`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `espace_id_uindex` (`id`),
  ADD KEY `espace_iduser_id_fk` (`idUser`);

--
-- Index pour la table `indicateur`
--
ALTER TABLE `indicateur`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `indicateur_id_uindex` (`id`),
  ADD KEY `indicateur_idespace_id_fk` (`idEspace`);

--
-- Index pour la table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `user_id_uindex` (`id`),
  ADD UNIQUE KEY `user_login_uindex` (`login`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `activite`
--
ALTER TABLE `activite`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=63;

--
-- AUTO_INCREMENT pour la table `espace`
--
ALTER TABLE `espace`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT pour la table `indicateur`
--
ALTER TABLE `indicateur`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT pour la table `user`
--
ALTER TABLE `user`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `activite`
--
ALTER TABLE `activite`
  ADD CONSTRAINT `activite_idespace_id_fk` FOREIGN KEY (`idEspace`) REFERENCES `espace` (`id`),
  ADD CONSTRAINT `activite_idindicateur_id_fk` FOREIGN KEY (`idIndicateur`) REFERENCES `indicateur` (`id`);

--
-- Contraintes pour la table `espace`
--
ALTER TABLE `espace`
  ADD CONSTRAINT `espace_iduser_id_fk` FOREIGN KEY (`idUser`) REFERENCES `user` (`id`);

--
-- Contraintes pour la table `indicateur`
--
ALTER TABLE `indicateur`
  ADD CONSTRAINT `indicateur_idespace_id_fk` FOREIGN KEY (`idEspace`) REFERENCES `espace` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
