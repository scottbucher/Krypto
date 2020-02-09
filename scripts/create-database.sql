-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3307
-- Generation Time: Feb 09, 2020 at 06:01 PM
-- Server version: 5.7.24-log
-- PHP Version: 7.2.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `kryptodev`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`scott`@`%` PROCEDURE `DoesDiscordAndUserExistInXp` (IN `IN_GuildId` INT(11), IN `In_UserId` INT(11))  BEGIN

SELECT COUNT(*) > 0 AS AlreadyExists
FROM xp
WHERE `GuildId` = IN_GuildId AND `UserId` = IN_UserId;

END$$

CREATE DEFINER=`scott`@`%` PROCEDURE `DoesGuildAlreadyExist` (IN `IN_DiscordID` VARCHAR(18))  BEGIN

SELECT COUNT(*) > 0 AS AlreadyExists
FROM guild
WHERE `DiscordId` = IN_DiscordId;

END$$

CREATE DEFINER=`scott`@`%` PROCEDURE `DoesUserAlreadyExist` (IN `IN_UserDiscordId` VARCHAR(18))  BEGIN

SELECT COUNT(*) > 0 AS AlreadyExists
FROM users
WHERE `UserDiscordId` = IN_UserDiscordId;

END$$

CREATE DEFINER=`scott`@`%` PROCEDURE `GetGuildId` (IN `IN_DiscordId` VARCHAR(18))  BEGIN

SELECT GuildId
FROM guild
WHERE `DiscordId` = IN_DiscordId;

END$$

CREATE DEFINER=`scott`@`%` PROCEDURE `GetGuildSettingsId` (IN `IN_DiscordId` VARCHAR(18))  BEGIN

SELECT GuildSettingsId
FROM guild
WHERE `DiscordId` = IN_DiscordId;

END$$

CREATE DEFINER=`scott`@`%` PROCEDURE `GetLevel` (IN `IN_GuildId` INT(11))  BEGIN

SELECT Level
FROM rewards
WHERE `GuildId` = IN_GuildId;

END$$

CREATE DEFINER=`scott`@`%` PROCEDURE `GetLevelCount` (IN `IN_GuildId` INT(11))  BEGIN

SELECT COUNT(Level) as Amount
FROM rewards
WHERE `GuildId` = IN_GuildId;

END$$

CREATE DEFINER=`scott`@`%` PROCEDURE `GetLevelingChannel` (IN `IN_GuildSettingsId` INT(11))  BEGIN

SELECT LevelingChannel
FROM guildsettings
WHERE `GuildSettingsId` = IN_GuildSettingsId;

END$$

CREATE DEFINER=`scott`@`%` PROCEDURE `GetPlayerTime` (IN `IN_GuildId` INT(11), IN `IN_UserId` INT(11))  BEGIN

SELECT LastUpdated
FROM xp
WHERE `GuildId` = IN_GuildId AND `UserId` = IN_UserId;

END$$

CREATE DEFINER=`scott`@`%` PROCEDURE `GetPrefix` (IN `IN_GuildSettingsId` INT(11))  BEGIN

SELECT Prefix
FROM guildsettings
WHERE `GuildSettingsId` = IN_GuildSettingsId;

END$$

CREATE DEFINER=`scott`@`%` PROCEDURE `GetRoles` (IN `IN_GuildId` INT(11), IN `IN_Level` INT(11))  BEGIN

SELECT Role
FROM rewards
WHERE `GuildId` = IN_GuildId AND `Level` = IN_Level;

END$$

CREATE DEFINER=`scott`@`%` PROCEDURE `GetSpecificLevelCount` (IN `IN_GuildId` INT(11), IN `IN_Level` INT(11))  BEGIN

SELECT COUNT(Level) as Amount
FROM rewards
WHERE `GuildId` = IN_GuildId AND `Level` = IN_Level;

END$$

CREATE DEFINER=`scott`@`%` PROCEDURE `GetUserId` (IN `IN_UserDiscordId` VARCHAR(18))  BEGIN

SELECT UserId
FROM users
WHERE `UserDiscordId` = IN_UserDiscordId;

END$$

CREATE DEFINER=`scott`@`%` PROCEDURE `GetUsersForLeaderBoard` (IN `IN_GuildId` INT(11), IN `IN_Size` INT(11))  BEGIN

SELECT UserDiscordId
FROM users
JOIN xp
ON users.UserId=xp.UserId
WHERE `GuildId` = IN_GuildId
ORDER BY XpAmount DESC LIMIT IN_Size;

END$$

CREATE DEFINER=`scott`@`%` PROCEDURE `GetXp` (IN `IN_GuildId` INT(11), IN `IN_UserId` INT(11))  BEGIN

SELECT XpAmount
FROM xp
WHERE `GuildId` = IN_GuildId AND `UserId` = IN_UserId;

END$$

CREATE DEFINER=`scott`@`%` PROCEDURE `InsertGuild` (IN `IN_DiscordId` VARCHAR(18), IN `IN_GuildSettingsId` INT(11), IN `IN_Active` TINYINT(1))  BEGIN

INSERT INTO guild(DiscordId, GuildSettingsId, Active)
VALUES(IN_DiscordId, IN_GuildSettingsId, IN_Active);

END$$

CREATE DEFINER=`scott`@`%` PROCEDURE `InsertGuildSettings` (IN `IN_Prefix` VARCHAR(100))  BEGIN

INSERT guildsettings
SET `Prefix` = IN_Prefix;

END$$

CREATE DEFINER=`scott`@`%` PROCEDURE `InsertReward` (IN `IN_GuildId` INT(11), IN `IN_Level` INT(11), IN `IN_Role` VARCHAR(100))  BEGIN

INSERT INTO rewards(GuildId, Level, Role)
VALUES(IN_GuildId, IN_Level, IN_Role);

END$$

CREATE DEFINER=`scott`@`%` PROCEDURE `InsertUser` (IN `IN_UserDiscordId` VARCHAR(18))  BEGIN

INSERT INTO users(UserDiscordId)
VALUES(IN_UserDiscordId);

END$$

CREATE DEFINER=`scott`@`%` PROCEDURE `InsertXp` (IN `IN_GuildId` INT(11), IN `IN_UserId` INT(11))  BEGIN

INSERT INTO xp(GuildId, UserId)
VALUES(IN_GuildId, IN_UserId);

END$$

CREATE DEFINER=`scott`@`%` PROCEDURE `RemoveReward` (IN `IN_GuildId` INT(11), IN `IN_Level` INT(11))  BEGIN

DELETE FROM rewards
WHERE `GuildId` = IN_GuildId AND `Level` = IN_Level;

END$$

CREATE DEFINER=`scott`@`%` PROCEDURE `SelectLastInsertID` ()  BEGIN

SELECT LAST_INSERT_ID();

END$$

CREATE DEFINER=`scott`@`%` PROCEDURE `UpdateGuildActive` (IN `IN_DiscordId` VARCHAR(18), IN `IN_Active` TINYINT(1))  BEGIN

UPDATE guild
SET `Active` = IN_Active
WHERE `DiscordId` = IN_DiscordId;

END$$

CREATE DEFINER=`scott`@`%` PROCEDURE `UpdateLastUpdated` (IN `IN_GuildId` INT(11), IN `IN_UserId` INT(11))  BEGIN

UPDATE xp
SET `LastUpdated` = CURRENT_TIMESTAMP
WHERE `GuildId` = IN_GuildId AND `UserId` = IN_UserId;

END$$

CREATE DEFINER=`scott`@`%` PROCEDURE `UpdateLevelingChannel` (IN `IN_ChannelId` VARCHAR(18), IN `IN_GuildSettingsId` INT(11))  BEGIN

UPDATE guildsettings
SET `LevelingChannel` = IN_ChannelId
WHERE `GuildSettingsId` = IN_GuildSettingsId;

END$$

CREATE DEFINER=`scott`@`%` PROCEDURE `UpdatePrefix` (IN `IN_Prefix` VARCHAR(100), IN `IN_GuildSettingsId` INT(11))  BEGIN

UPDATE guildsettings
SET `Prefix` = IN_Prefix
WHERE `GuildSettingsId` = IN_GuildSettingsId;

END$$

CREATE DEFINER=`scott`@`%` PROCEDURE `UpdateXp` (IN `IN_Xp` INT(11), IN `IN_GuildId` INT(11), IN `IN_UserId` INT(11))  BEGIN

UPDATE xp
SET `XpAmount` = IN_Xp
WHERE `GuildId` = IN_GuildId AND `UserId` = IN_UserId;

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
  `Active` tinyint(1) DEFAULT '1'
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
  `XpAmount` int(11) DEFAULT '0',
  `XpLevel` int(11) DEFAULT '0',
  `LastUpdated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
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
