CREATE DATABASE goodeelmsdb;
DROP DATABASE goodeelmsdb;

USE goodeelmsdb;

-- 총 16개 테이블

-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)

-- Host: localhost    Database: goodeelmsdb
-- ------------------------------------------------------
-- Server version	8.0.44

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

-- Table structure for table `admin`

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `admin_id` int NOT NULL AUTO_INCREMENT,
  `admin_login_id` varchar(50) NOT NULL,
  `admin_password` varchar(255) NOT NULL,
  `admin_name` varchar(50) NOT NULL,
  PRIMARY KEY (`admin_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Table structure for table `student`

DROP TABLE IF EXISTS `student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student` (
  `student_id` int NOT NULL AUTO_INCREMENT,
  `student_no` varchar(50) NOT NULL,
  `student_password` varchar(255) NOT NULL,
  `student_name` varchar(50) NOT NULL,
  `student_phone` varchar(20) NOT NULL,
  `student_identity_number` varchar(50) NOT NULL,
  `student_gender` varchar(10) DEFAULT NULL,
  `student_address` varchar(255) DEFAULT NULL,
  `student_status` varchar(20) DEFAULT '재학',
  `student_email` varchar(100) DEFAULT NULL,
  `student_bank` varchar(100) DEFAULT NULL,
  `student_photofile` varchar(255) NOT NULL DEFAULT 'defaultUserProfile.jpg',
  `student_photoUUID` varchar(255) NOT NULL DEFAULT 'default.jpg',
  PRIMARY KEY (`student_id`),
  UNIQUE KEY `student_no` (`student_no`),
  UNIQUE KEY `student_phone` (`student_phone`),
  UNIQUE KEY `student_identity_number` (`student_identity_number`),
  UNIQUE KEY `student_email` (`student_email`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Table structure for table `professor`

DROP TABLE IF EXISTS `professor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `professor` (
  `professor_id` int NOT NULL AUTO_INCREMENT,
  `professor_name` varchar(50) NOT NULL,
  `professor_email` varchar(100) NOT NULL,
  `professor_password` varchar(255) NOT NULL,
  `professor_status` varchar(20) DEFAULT '재직',
  `major_id` int NOT NULL,
  PRIMARY KEY (`professor_id`),
  UNIQUE KEY `professor_email` (`professor_email`),
  KEY `major_id` (`major_id`),
  CONSTRAINT `professor_ibfk_1` FOREIGN KEY (`major_id`) REFERENCES `major` (`major_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Table structure for table `academic_calendar`

DROP TABLE IF EXISTS `academic_calendar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `academic_calendar` (
  `academic_calendar_id` int NOT NULL AUTO_INCREMENT,
  `academic_event_name` varchar(255) NOT NULL,
  `academic_event_date` varchar(10) NOT NULL,
  `academic_event_time` varchar(5) NOT NULL DEFAULT '00:00',
  `admin_id` int NOT NULL,
  PRIMARY KEY (`academic_calendar_id`),
  KEY `admin_id` (`admin_id`),
  CONSTRAINT `academic_calendar_ibfk_1` FOREIGN KEY (`admin_id`) REFERENCES `admin` (`admin_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Table structure for table `board`

DROP TABLE IF EXISTS `board`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board` (
  `board_id` int NOT NULL AUTO_INCREMENT,
  `board_title` varchar(200) NOT NULL,
  `board_content` longtext NOT NULL,
  `board_hit` int DEFAULT NULL,
  `board_important` varchar(10) DEFAULT 'N',
  `board_reg_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `admin_id` int NOT NULL,
  PRIMARY KEY (`board_id`),
  KEY `admin_id` (`admin_id`),
  CONSTRAINT `board_ibfk_1` FOREIGN KEY (`admin_id`) REFERENCES `admin` (`admin_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Table structure for table `building`

DROP TABLE IF EXISTS `building`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `building` (
  `building_id` int NOT NULL AUTO_INCREMENT,
  `building_name` varchar(30) NOT NULL,
  PRIMARY KEY (`building_id`),
  UNIQUE KEY `building_name` (`building_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Table structure for table `change_major_history`

DROP TABLE IF EXISTS `change_major_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `change_major_history` (
  `change_major_id` int NOT NULL AUTO_INCREMENT,
  `changed_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `student_id` int NOT NULL,
  `from_major_id` int NOT NULL,
  `to_major_id` int NOT NULL,
  PRIMARY KEY (`change_major_id`),
  KEY `student_id` (`student_id`),
  KEY `from_major_id` (`from_major_id`),
  KEY `to_major_id` (`to_major_id`),
  CONSTRAINT `change_major_history_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`),
  CONSTRAINT `change_major_history_ibfk_2` FOREIGN KEY (`from_major_id`) REFERENCES `major` (`major_id`),
  CONSTRAINT `change_major_history_ibfk_3` FOREIGN KEY (`to_major_id`) REFERENCES `major` (`major_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Table structure for table `lecture`

DROP TABLE IF EXISTS `lecture`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lecture` (
  `lecture_id` int NOT NULL AUTO_INCREMENT,
  `lecture_code` int NOT NULL,
  `lecture_name` varchar(100) NOT NULL,
  `lecture_description` text,
  `lecture_room` varchar(50) NOT NULL,
  `lecture_credit` int NOT NULL,
  `lecture_year` varchar(4) NOT NULL,
  `lecture_status` varchar(20) NOT NULL DEFAULT '예정',
  `lecture_semester` int NOT NULL,
  `lecture_section` varchar(2) NOT NULL,
  `lecture_type` varchar(20) NOT NULL,
  `lecture_current_people` int DEFAULT '0',
  `lecture_capacity` int NOT NULL,
  `major_id` int NOT NULL,
  `professor_id` int NOT NULL,
  `building_id` int NOT NULL,
  PRIMARY KEY (`lecture_id`),
  UNIQUE KEY `professor_id` (`professor_id`,`lecture_code`,`lecture_year`,`lecture_semester`,`lecture_section`),
  KEY `major_id` (`major_id`),
  UNIQUE KEY `building_id` (`building_id`, `lecture_room`, `lecture_year`, `lecture_semester`),
  CONSTRAINT `lecture_ibfk_1` FOREIGN KEY (`major_id`) REFERENCES `major` (`major_id`),
  CONSTRAINT `lecture_ibfk_2` FOREIGN KEY (`professor_id`) REFERENCES `professor` (`professor_id`),
  CONSTRAINT `lecture_ibfk_3` FOREIGN KEY (`building_id`) REFERENCES `building` (`building_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table structure for table `lecture_evaluation`

DROP TABLE IF EXISTS `lecture_evaluation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lecture_evaluation` (
  `evaluation_id` int NOT NULL AUTO_INCREMENT,
  `rating` int NOT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `evaluated_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `student_id` int NOT NULL,
  `lecture_id` int NOT NULL,
  PRIMARY KEY (`evaluation_id`),
  KEY `student_id` (`student_id`),
  KEY `lecture_id` (`lecture_id`),
  CONSTRAINT `lecture_evaluation_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`),
  CONSTRAINT `lecture_evaluation_ibfk_2` FOREIGN KEY (`lecture_id`) REFERENCES `lecture` (`lecture_id`)
) ENGINE=InnoDB AUTO_INCREMENT=122319 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Table structure for table `lecture_history`

DROP TABLE IF EXISTS `lecture_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lecture_history` (
  `student_id` int NOT NULL,
  `lecture_id` int NOT NULL,
  `lecture_score` DECIMAL(2,1) DEFAULT NULL,
  PRIMARY KEY (`student_id`,`lecture_id`),
  KEY `lecture_id` (`lecture_id`),
  CONSTRAINT `lecture_history_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`),
  CONSTRAINT `lecture_history_ibfk_2` FOREIGN KEY (`lecture_id`) REFERENCES `lecture` (`lecture_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Table structure for table `major`

DROP TABLE IF EXISTS `major`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `major` (
  `major_id` int NOT NULL AUTO_INCREMENT,
  `major_code` varchar(20) NOT NULL,
  `major_name` varchar(100) NOT NULL,
  PRIMARY KEY (`major_id`),
  UNIQUE KEY `major_code` (`major_code`),
  UNIQUE KEY `major_name` (`major_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Table structure for table `pre_enrollment`

DROP TABLE IF EXISTS `pre_enrollment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pre_enrollment` (
  `pre_enrollment_id` int NOT NULL AUTO_INCREMENT,
  `student_id` int NOT NULL,
  `lecture_id` int NOT NULL,
  `pre_enrollment_status` varchar(10) NOT NULL DEFAULT 'progress',
  PRIMARY KEY (`pre_enrollment_id`),
  KEY `student_id` (`student_id`),
  KEY `lecture_id` (`lecture_id`),
  CONSTRAINT `pre_enrollment_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`),
  CONSTRAINT `pre_enrollment_ibfk_2` FOREIGN KEY (`lecture_id`) REFERENCES `lecture` (`lecture_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Table structure for table `scholarship_history`

DROP TABLE IF EXISTS `scholarship_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `scholarship_history` (
  `scholarship_id` int NOT NULL AUTO_INCREMENT,
  `scholarship_semester` int NOT NULL,
  `scholarship_amount` int DEFAULT '3500000',
  `student_id` int DEFAULT NULL,
  PRIMARY KEY (`scholarship_id`),
  KEY `student_id` (`student_id`),
  CONSTRAINT `scholarship_history_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Table structure for table `student_major`

DROP TABLE IF EXISTS `student_major`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student_major` (
  `student_major_id` int NOT NULL AUTO_INCREMENT,
  `student_id` int NOT NULL,
  `major_id` int NOT NULL,
  PRIMARY KEY (`student_major_id`),
  KEY `student_id` (`student_id`),
  KEY `major_id` (`major_id`),
  CONSTRAINT `student_major_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`),
  CONSTRAINT `student_major_ibfk_2` FOREIGN KEY (`major_id`) REFERENCES `major` (`major_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Table structure for table `student_status_history`

DROP TABLE IF EXISTS `student_status_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student_status_history` (
  `status_history_id` int NOT NULL AUTO_INCREMENT,
  `status_type` varchar(20) NOT NULL,
  `status_reason` varchar(255) NOT NULL,
  `status_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `student_id` int NOT NULL,
  `admin_id` int NOT NULL,
  PRIMARY KEY (`status_history_id`),
  KEY `student_id` (`student_id`),
  KEY `admin_id` (`admin_id`),
  CONSTRAINT `student_status_history_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`),
  CONSTRAINT `student_status_history_ibfk_2` FOREIGN KEY (`admin_id`) REFERENCES `admin` (`admin_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Table structure for table `tuition_payment`

DROP TABLE IF EXISTS `tuition_payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tuition_payment` (
  `payment_id` int NOT NULL AUTO_INCREMENT,
  `payment_amount` int NOT NULL,
  `payment_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `payment_status` varchar(20) DEFAULT NULL,
  `student_id` int NOT NULL,
  PRIMARY KEY (`payment_id`),
  KEY `student_id` (`student_id`),
  CONSTRAINT `tuition_payment_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Dumping events for database 'goodeelmsdb'

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-27  9:47:54
