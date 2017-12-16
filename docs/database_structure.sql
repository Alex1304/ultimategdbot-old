-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2
-- http://www.phpmyadmin.net
--
-- Client :  localhost
-- Généré le :  Sam 16 Décembre 2017 à 14:33
-- Version du serveur :  5.7.20-0ubuntu0.16.04.1
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
-- Structure de la table `gd_mod_list`
--

CREATE TABLE `gd_mod_list` (
  `account_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `guild_settings`
--

CREATE TABLE `guild_settings` (
  `guild_id` bigint(20) NOT NULL,
  `role_awarded_levels` bigint(20) NOT NULL DEFAULT '0',
  `channel_awarded_levels` bigint(20) NOT NULL DEFAULT '0',
  `role_gd_moderators` bigint(20) NOT NULL DEFAULT '0',
  `channel_gd_moderators` bigint(20) NOT NULL DEFAULT '0',
  `channel_bot_announcements` bigint(20) NOT NULL DEFAULT '0',
  `tag_everyone_on_bot_announcement` tinyint(1) NOT NULL DEFAULT '0',
  `channel_timely_levels` bigint(20) NOT NULL DEFAULT '0',
  `role_timely_levels` bigint(20) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `join_sge`
--

CREATE TABLE `join_sge` (
  `user_id` bigint(20) NOT NULL,
  `event_id` bigint(20) NOT NULL,
  `join_date` datetime NOT NULL,
  `curr_stat_value` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `stat_grind_event`
--

CREATE TABLE `stat_grind_event` (
  `event_id` bigint(20) NOT NULL,
  `guild_id` bigint(20) NOT NULL,
  `date_begin` datetime NOT NULL,
  `date_end` datetime NOT NULL,
  `stat_type` varchar(8) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `timely_level`
--

CREATE TABLE `timely_level` (
  `id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
-- Index pour la table `gd_mod_list`
--
ALTER TABLE `gd_mod_list`
  ADD PRIMARY KEY (`account_id`);

--
-- Index pour la table `guild_settings`
--
ALTER TABLE `guild_settings`
  ADD PRIMARY KEY (`guild_id`);

--
-- Index pour la table `join_sge`
--
ALTER TABLE `join_sge`
  ADD PRIMARY KEY (`user_id`,`event_id`),
  ADD KEY `fk_event_id` (`event_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Index pour la table `stat_grind_event`
--
ALTER TABLE `stat_grind_event`
  ADD PRIMARY KEY (`event_id`),
  ADD KEY `guild_id` (`guild_id`);

--
-- Index pour la table `timely_level`
--
ALTER TABLE `timely_level`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `user_settings`
--
ALTER TABLE `user_settings`
  ADD PRIMARY KEY (`user_id`);

--
-- Contraintes pour les tables exportées
--

--
-- Contraintes pour la table `join_sge`
--
ALTER TABLE `join_sge`
  ADD CONSTRAINT `fk_event_id` FOREIGN KEY (`event_id`) REFERENCES `stat_grind_event` (`event_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `user_settings` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `stat_grind_event`
--
ALTER TABLE `stat_grind_event`
  ADD CONSTRAINT `fk_guild_id` FOREIGN KEY (`guild_id`) REFERENCES `guild_settings` (`guild_id`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
