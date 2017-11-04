-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2
-- http://www.phpmyadmin.net
--
-- Client :  localhost
-- Généré le :  Mer 25 Octobre 2017 à 00:17
-- Version du serveur :  5.7.19-0ubuntu0.16.04.1
-- Version de PHP :  7.0.22-0ubuntu0.16.04.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `ultimategdbot`
--

-- --------------------------------------------------------

--
-- Structure de la table `gd_level`
--

CREATE TABLE `gd_level` (
  `insert_date` datetime NOT NULL,
  `level_id` bigint(20) NOT NULL,
  `featured` int(11) NOT NULL,
  `is_epic` tinyint(1) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `description` text,
  `difficulty` smallint(6) NOT NULL,
  `demon_difficulty` smallint(6) NOT NULL,
  `stars` smallint(6) NOT NULL,
  `downloads` bigint(20) NOT NULL,
  `likes` bigint(20) NOT NULL,
  `length` smallint(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `guild_settings`
--

CREATE TABLE `guild_settings` (
  `guild_id` bigint(20) NOT NULL,
  `gdevent_awarded_subscriber_roleid` bigint(20) NOT NULL,
  `gdevent_awarded_subscriber_channelid` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `user_settings`
--

CREATE TABLE `user_settings` (
  `user_id` bigint(20) NOT NULL,
  `gd_user_id` bigint(20) NOT NULL,
  `link_activated` tinyint(1) NOT NULL,
  `confirmation_token` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Index pour les tables exportées
--

--
-- Index pour la table `gd_level`
--
ALTER TABLE `gd_level`
  ADD PRIMARY KEY (`level_id`);

--
-- Index pour la table `guild_settings`
--
ALTER TABLE `guild_settings`
  ADD PRIMARY KEY (`guild_id`);
  
--
-- Index pour la table `user_settings`
--
ALTER TABLE `user_settings`
  ADD PRIMARY KEY (`user_id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
