-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Mar 16, 2025 at 02:26 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `projecttwodatabase`
--

-- --------------------------------------------------------

--
-- Table structure for table `activity_logs`
--

CREATE TABLE `activity_logs` (
  `LogID` int(11) NOT NULL,
  `UserID` int(11) DEFAULT NULL,
  `RoleID` int(11) DEFAULT NULL,
  `Action` varchar(255) NOT NULL,
  `Timestamp` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `dataset_index`
--

CREATE TABLE `dataset_index` (
  `DatasetID` int(11) NOT NULL,
  `DatasetName` varchar(255) NOT NULL,
  `RoleID` int(11) DEFAULT NULL,
  `Timestamp` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `permission_requests`
--

CREATE TABLE `permission_requests` (
  `requestid` int(11) NOT NULL,
  `userid` int(11) NOT NULL,
  `roleid` int(11) NOT NULL,
  `status` enum('PENDING','APPROVED','DENIED') DEFAULT 'PENDING',
  `request_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `decision_date` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `roles`
--

CREATE TABLE `roles` (
  `RoleID` int(11) NOT NULL,
  `Role_Name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `UserID` int(11) NOT NULL,
  `First_Name` varchar(50) NOT NULL,
  `Last_Name` varchar(50) NOT NULL,
  `Email` varchar(100) NOT NULL,
  `Password_Hash` varchar(255) NOT NULL,
  `RoleID` int(11) DEFAULT NULL,
  `User_Access` enum('Admin','User') DEFAULT 'User'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `activity_logs`
--
ALTER TABLE `activity_logs`
  ADD PRIMARY KEY (`LogID`),
  ADD KEY `UserID` (`UserID`),
  ADD KEY `RoleID` (`RoleID`);

--
-- Indexes for table `dataset_index`
--
ALTER TABLE `dataset_index`
  ADD PRIMARY KEY (`DatasetID`);

--
-- Indexes for table `permission_requests`
--
ALTER TABLE `permission_requests`
  ADD PRIMARY KEY (`requestid`),
  ADD KEY `userid` (`userid`),
  ADD KEY `roleid` (`roleid`);

--
-- Indexes for table `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`RoleID`),
  ADD UNIQUE KEY `Role_Name` (`Role_Name`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`UserID`),
  ADD UNIQUE KEY `Email` (`Email`),
  ADD UNIQUE KEY `Password_Hash` (`Password_Hash`),
  ADD KEY `RoleID` (`RoleID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `activity_logs`
--
ALTER TABLE `activity_logs`
  MODIFY `LogID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `dataset_index`
--
ALTER TABLE `dataset_index`
  MODIFY `DatasetID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `permission_requests`
--
ALTER TABLE `permission_requests`
  MODIFY `requestid` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `roles`
--
ALTER TABLE `roles`
  MODIFY `RoleID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `UserID` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `activity_logs`
--
ALTER TABLE `activity_logs`
  ADD CONSTRAINT `activity_logs_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`) ON DELETE CASCADE,
  ADD CONSTRAINT `activity_logs_ibfk_2` FOREIGN KEY (`RoleID`) REFERENCES `roles` (`RoleID`) ON DELETE SET NULL;

--
-- Constraints for table `permission_requests`
--
ALTER TABLE `permission_requests`
  ADD CONSTRAINT `permission_requests_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `users` (`UserID`) ON DELETE CASCADE,
  ADD CONSTRAINT `permission_requests_ibfk_2` FOREIGN KEY (`roleid`) REFERENCES `roles` (`RoleID`) ON DELETE CASCADE;

--
-- Constraints for table `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `users_ibfk_1` FOREIGN KEY (`RoleID`) REFERENCES `roles` (`RoleID`) ON DELETE SET NULL;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
