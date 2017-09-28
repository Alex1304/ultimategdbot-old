-- phpMyAdmin SQL Dump
-- version 4.0.10.6
-- http://www.phpmyadmin.net
--
-- Host: mysql-alex1304.alwaysdata.net
-- Generation Time: Sep 28, 2017 at 06:27 PM
-- Server version: 10.1.23-MariaDB
-- PHP Version: 5.6.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `alex1304_ultimategdbot`
--

-- --------------------------------------------------------

--
-- Table structure for table `guild_settings`
--

CREATE TABLE IF NOT EXISTS `guild_settings` (
  `guild_id` bigint(20) NOT NULL,
  `gdevent_awarded_subscriber_roleid` bigint(20) NOT NULL,
  `gdevent_awarded_subscriber_channelid` bigint(20) NOT NULL,
  PRIMARY KEY (`guild_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `last_seen_awarded_levels`
--

CREATE TABLE IF NOT EXISTS `last_seen_awarded_levels` (
  `level_id` bigint(20) NOT NULL,
  `is_featured` tinyint(1) NOT NULL,
  `is_epic` tinyint(1) NOT NULL,
  PRIMARY KEY (`level_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `user_settings`
--

CREATE TABLE IF NOT EXISTS `user_settings` (
  `user_id` bigint(20) NOT NULL,
  `gd_user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
