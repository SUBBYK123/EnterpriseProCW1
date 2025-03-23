-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: mydb
-- ------------------------------------------------------
-- Server version	8.0.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `activity_logs`
--

DROP TABLE IF EXISTS `activity_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activity_logs` (
  `LogID` int NOT NULL AUTO_INCREMENT,
  `UserID` int DEFAULT NULL,
  `RoleID` int DEFAULT NULL,
  `Action` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `Timestamp` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`LogID`),
  KEY `UserID` (`UserID`),
  KEY `RoleID` (`RoleID`),
  CONSTRAINT `activity_logs_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_logs`
--

LOCK TABLES `activity_logs` WRITE;
/*!40000 ALTER TABLE `activity_logs` DISABLE KEYS */;
/*!40000 ALTER TABLE `activity_logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dataset_index`
--

DROP TABLE IF EXISTS `dataset_index`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dataset_index` (
  `DatasetID` int NOT NULL AUTO_INCREMENT,
  `DatasetName` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `RoleID` int DEFAULT NULL,
  `Timestamp` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`DatasetID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dataset_index`
--

LOCK TABLES `dataset_index` WRITE;
/*!40000 ALTER TABLE `dataset_index` DISABLE KEYS */;
/*!40000 ALTER TABLE `dataset_index` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permission_requests`
--

DROP TABLE IF EXISTS `permission_requests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `permission_requests` (
  `requestid` int NOT NULL AUTO_INCREMENT,
  `userid` int NOT NULL,
  `status` enum('PENDING','APPROVED','DENIED') COLLATE utf8mb4_general_ci DEFAULT 'PENDING',
  `request_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `decision_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`requestid`),
  KEY `userid` (`userid`),
  CONSTRAINT `permission_requests_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `users` (`UserID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permission_requests`
--

LOCK TABLES `permission_requests` WRITE;
/*!40000 ALTER TABLE `permission_requests` DISABLE KEYS */;
/*!40000 ALTER TABLE `permission_requests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `UserID` int NOT NULL AUTO_INCREMENT,
  `First_Name` varchar(50) NOT NULL,
  `Last_Name` varchar(50) NOT NULL,
  `RoleID` int DEFAULT NULL,
  `User_Access` enum('Admin','User') DEFAULT 'User',
  `email_address` varchar(100) DEFAULT NULL,
  `password_hash` varchar(255) DEFAULT NULL,
  `department` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`UserID`),
  UNIQUE KEY `email_address_UNIQUE` (`email_address`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'m','k',NULL,'User','mk@test.com','Password123','adult'),(2,'Mustafa','Kamran',NULL,'User','mustafakamran46@hotmail.com','$2a$10$Pu83qNYHL9xN9iF/WFRVmO42VtOQaE/zW9rWvAGb5CMGvyTralmhm','adult'),(3,'Mustafa','Kamran',NULL,'User','mustafakamran461@hotmail.com','$2a$10$ZCgi/EjKRhuHxo8HNDntsuLXOyRE98n1aNfADykZpP.vP9af145wq','adult'),(4,'Mohammad Subhaan','Khurram',NULL,'User','mohammadsubhaankhurram1@gmail.com','$2a$10$UrVjJybylRWzoXtrWAO9b.e1oJ/1Z1rbuK3euHmSckFg/Mzd98acG','adult');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-23 14:45:42
