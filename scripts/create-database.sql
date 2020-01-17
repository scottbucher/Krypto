-- phpMyAdmin SQL Dump
-- version 4.9.2
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Jan 05, 2020 at 11:41 PM
-- Server version: 10.3.18-MariaDB-0+deb10u1
-- PHP Version: 7.3.11-1~deb10u1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `krypto-dev`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`kevin`@`%` PROCEDURE `UpdateGuildActive` (IN `IN_DiscordId` VARCHAR(18), IN `IN_Active` TINYINT(1))  BEGIN

UPDATE guild
SET `Active` = IN_Active
WHERE `DiscordId` = IN_DiscordId;

END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `guild`
--

CREATE TABLE `guild` (
  `GuildId` int(11) NOT NULL,
  `DiscordId` varchar(18) NOT NULL,
  `GuildSettingsId` int(11) DEFAULT NULL,
  `Active` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `guildsettings`
--

CREATE TABLE `guildsettings` (
  `GuildSettingsId` int(11) NOT NULL,
  `Prefix` varchar(100) NOT NULL,
  `LevelingChannel` varchar(18) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `rewards`
--

CREATE TABLE `rewards` (
  `RewardsId` int(11) NOT NULL,
  `GuildId` int(11) NOT NULL,
  `Level` int(11) NOT NULL,
  `Role` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `roles`
--

CREATE TABLE `roles` (
  `RoleId` int(11) NOT NULL,
  `GuildId` int(11) NOT NULL,
  `Role` varchar(100) NOT NULL,
  `Emote` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `UserId` int(11) NOT NULL,
  `UserDiscordId` varchar(18) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `xp`
--

CREATE TABLE `xp` (
  `XPId` int(11) NOT NULL,
  `GuildId` int(11) NOT NULL,
  `UserId` int(11) NOT NULL,
  `XpAmount` int(11) DEFAULT 0,
  `XpLevel` int(11) DEFAULT 0,
  `LastUpdated` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `guild`
--
ALTER TABLE `guild`
  ADD PRIMARY KEY (`GuildId`),
  ADD KEY `GuildSettingsId` (`GuildSettingsId`);

--
-- Indexes for table `guildsettings`
--
ALTER TABLE `guildsettings`
  ADD PRIMARY KEY (`GuildSettingsId`);

--
-- Indexes for table `rewards`
--
ALTER TABLE `rewards`
  ADD PRIMARY KEY (`RewardsId`),
  ADD KEY `GuildId` (`GuildId`);

--
-- Indexes for table `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`RoleId`),
  ADD KEY `GuildId` (`GuildId`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`UserId`);

--
-- Indexes for table `xp`
--
ALTER TABLE `xp`
  ADD PRIMARY KEY (`XPId`),
  ADD KEY `GuildId` (`GuildId`),
  ADD KEY `UserId` (`UserId`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `guild`
--
ALTER TABLE `guild`
  MODIFY `GuildId` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `guildsettings`
--
ALTER TABLE `guildsettings`
  MODIFY `GuildSettingsId` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `rewards`
--
ALTER TABLE `rewards`
  MODIFY `RewardsId` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `roles`
--
ALTER TABLE `roles`
  MODIFY `RoleId` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `UserId` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `xp`
--
ALTER TABLE `xp`
  MODIFY `XPId` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `guild`
--
ALTER TABLE `guild`
  ADD CONSTRAINT `guild_ibfk_1` FOREIGN KEY (`GuildSettingsId`) REFERENCES `guildsettings` (`GuildSettingsId`);

--
-- Constraints for table `rewards`
--
ALTER TABLE `rewards`
  ADD CONSTRAINT `rewards_ibfk_1` FOREIGN KEY (`GuildId`) REFERENCES `guild` (`GuildId`);

--
-- Constraints for table `roles`
--
ALTER TABLE `roles`
  ADD CONSTRAINT `roles_ibfk_1` FOREIGN KEY (`GuildId`) REFERENCES `guild` (`GuildId`);

--
-- Constraints for table `xp`
--
ALTER TABLE `xp`
  ADD CONSTRAINT `xp_ibfk_1` FOREIGN KEY (`GuildId`) REFERENCES `guild` (`GuildId`),
  ADD CONSTRAINT `xp_ibfk_2` FOREIGN KEY (`UserId`) REFERENCES `users` (`UserId`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
