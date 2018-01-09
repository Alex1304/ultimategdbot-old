-- phpMyAdmin SQL Dump
-- version 4.7.1
-- https://www.phpmyadmin.net/
--
-- Hôte : us-cdbr-iron-east-05.cleardb.net
-- Généré le :  mar. 09 jan. 2018 à 20:51
-- Version du serveur :  5.6.36-log
-- Version de PHP :  7.0.22-0ubuntu0.16.04.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

--
-- Base de données :  `heroku_1f8c3734368a235`
--

-- --------------------------------------------------------

--
-- Structure de la table `awarded_level`
--

CREATE TABLE `awarded_level` (
  `insert_date` datetime NOT NULL,
  `level_id` bigint(20) NOT NULL,
  `downloads` bigint(20) NOT NULL,
  `likes` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `gd_mod_list`
--

CREATE TABLE `gd_mod_list` (
  `account_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
  `date_begin` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `date_end` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `stat_type` varchar(8) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `timely_level`
--

CREATE TABLE `timely_level` (
  `id` bigint(20) NOT NULL
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
-- Index pour les tables déchargées
--

--
-- Index pour la table `awarded_level`
--
ALTER TABLE `awarded_level`
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
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `stat_grind_event`
--
ALTER TABLE `stat_grind_event`
  MODIFY `event_id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- Contraintes pour les tables déchargées
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
COMMIT;
