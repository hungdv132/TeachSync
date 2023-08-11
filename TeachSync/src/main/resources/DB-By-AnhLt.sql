-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: localhost    Database: teachsync
-- ------------------------------------------------------
-- Server version	8.0.31

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
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `address` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `addressNo` varchar(45) NOT NULL,
  `street` varchar(255) NOT NULL,
  `unitId` bigint NOT NULL,
  `addressString` longtext COMMENT 'auto generated in code, save on query resource',
  `status` varchar(45) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `createdBy` bigint DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `updatedBy` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_address_location_unit_idx` (`unitId`),
  KEY `fk_address_user_createdBy_idx` (`createdBy`),
  KEY `fk_address_user_updatedBy_idx` (`updatedBy`),
  CONSTRAINT `fk_address_location_unit` FOREIGN KEY (`unitId`) REFERENCES `location_unit` (`id`),
  CONSTRAINT `fk_address_user_createdBy` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_address_user_updatedBy` FOREIGN KEY (`updatedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `address`
--

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address` DISABLE KEYS */;
INSERT INTO `address` VALUES (1,'190ABC','Doi Can',8,'190ABC Doi Can, P.Doi Can, Q.Ba Dinh, TP.HN, VN','CREATED',NULL,NULL,NULL,NULL),(2,'4DEF','Ngoc Ha',9,'4DEF Ngoc Ha, P.Ngoc Ha, Q.Ba Dinh, TP.HN, HN, VN','CREATED',NULL,NULL,NULL,NULL),(3,'337GHI','Cau Giay',8,'337GHI Cau Giay, P.Dich Vong, Q.Cau Giay, TP.HN, VN','CREATED',NULL,NULL,NULL,NULL),(4,'31JKL','Ho Tung Mau',8,'31JKL Ho Tung Mau, P.Mai Dich, Q.Cau Giay, TP.HN, VN','CREATED',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `answer`
--

DROP TABLE IF EXISTS `answer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `answer` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `questionId` bigint NOT NULL,
  `answerDesc` longtext NOT NULL,
  `answerScore` float NOT NULL,
  `isCorrect` bit(1) NOT NULL,
  `status` varchar(45) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `createdBy` bigint DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `updatedBy` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_answer_question_idx` (`questionId`),
  KEY `fk_answer_user_createdBy_idx` (`createdBy`),
  KEY `fk_answer_user_updatedBy_idx` (`updatedBy`),
  CONSTRAINT `fk_answer_question` FOREIGN KEY (`questionId`) REFERENCES `question` (`id`),
  CONSTRAINT `fk_answer_user_createdBy` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_answer_user_updatedBy` FOREIGN KEY (`updatedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `answer`
--

LOCK TABLES `answer` WRITE;
/*!40000 ALTER TABLE `answer` DISABLE KEYS */;
/*!40000 ALTER TABLE `answer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `application_detail`
--

DROP TABLE IF EXISTS `application_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `application_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `applicationId` bigint DEFAULT NULL,
  `detailType` varchar(45) NOT NULL COMMENT 'cv, id, degree, ...',
  `detailLink` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT 'url to file',
  `detailNote` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `submitAt` datetime DEFAULT NULL,
  `status` varchar(45) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `createdBy` bigint DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `updatedBy` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_application_detail_application_idx` (`applicationId`),
  KEY `fk_application_detail_user_createdBy_idx` (`createdBy`),
  KEY `fk_application_detail_user_updatedBy_idx` (`updatedBy`),
  CONSTRAINT `fk_application_detail_application` FOREIGN KEY (`applicationId`) REFERENCES `campaign_application` (`id`),
  CONSTRAINT `fk_application_detail_user_createdBy` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_application_detail_user_updatedBy` FOREIGN KEY (`updatedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `application_detail`
--

LOCK TABLES `application_detail` WRITE;
/*!40000 ALTER TABLE `application_detail` DISABLE KEYS */;
INSERT INTO `application_detail` VALUES (5,5,'CV',NULL,NULL,'2023-07-20 18:44:23','CREATED','2023-07-20 18:44:23',7,'2023-07-20 18:44:23',7),(6,6,'CV',NULL,NULL,'2023-07-20 18:45:03','CREATED','2023-07-20 18:45:03',7,'2023-07-20 18:45:03',7);
/*!40000 ALTER TABLE `application_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `attendant`
--

DROP TABLE IF EXISTS `attendant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `attendant` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `sessionId` bigint NOT NULL,
  `memberId` bigint NOT NULL,
  `isPresent` bit(1) NOT NULL,
  `status` varchar(45) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `createdBy` bigint DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `updatedBy` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_attendant_clazz_member_idx` (`memberId`),
  KEY `fk_attendant_session_idx` (`sessionId`),
  KEY `fk_attendant_user_createdBy_idx` (`createdBy`),
  KEY `fk_attendant_user_updatedBy_idx` (`updatedBy`),
  CONSTRAINT `fk_attendant_clazz_member` FOREIGN KEY (`memberId`) REFERENCES `clazz_member` (`id`),
  CONSTRAINT `fk_attendant_session` FOREIGN KEY (`sessionId`) REFERENCES `session` (`id`),
  CONSTRAINT `fk_attendant_user_createdBy` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_attendant_user_updatedBy` FOREIGN KEY (`updatedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attendant`
--

LOCK TABLES `attendant` WRITE;
/*!40000 ALTER TABLE `attendant` DISABLE KEYS */;
/*!40000 ALTER TABLE `attendant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `campaign_application`
--

DROP TABLE IF EXISTS `campaign_application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `campaign_application` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `campaignId` bigint NOT NULL,
  `applicantId` bigint NOT NULL,
  `appliedAt` datetime NOT NULL,
  `result` varchar(45) NOT NULL,
  `resultDate` datetime DEFAULT NULL,
  `status` varchar(45) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `createdBy` bigint DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `updatedBy` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_campaign_application_user_createdBy_idx` (`createdBy`),
  KEY `fk_campaign_application_user_updatedBy_idx` (`updatedBy`),
  KEY `fk_application_campaign_idx` (`campaignId`),
  KEY `fk_application_user_applicantId_idx` (`applicantId`),
  CONSTRAINT `fk_application_campaign` FOREIGN KEY (`campaignId`) REFERENCES `recruitment_campaign` (`id`),
  CONSTRAINT `fk_application_user_applicantId` FOREIGN KEY (`applicantId`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_campaign_application_user_createdBy` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_campaign_application_user_updatedBy` FOREIGN KEY (`updatedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `campaign_application`
--

LOCK TABLES `campaign_application` WRITE;
/*!40000 ALTER TABLE `campaign_application` DISABLE KEYS */;
INSERT INTO `campaign_application` VALUES (5,1,7,'2023-07-20 18:44:23','Đã từ chối','2023-07-20 18:44:23','CREATED','2023-07-20 18:44:23',7,'2023-07-20 18:44:41',7),(6,1,7,'2023-07-20 18:45:03','Đã duyệt','2023-07-20 18:45:03','CREATED','2023-07-20 18:45:03',7,'2023-07-20 18:45:16',7);
/*!40000 ALTER TABLE `campaign_application` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `center`
--

DROP TABLE IF EXISTS `center`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `center` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `addressId` bigint NOT NULL,
  `centerName` varchar(45) NOT NULL,
  `centerType` varchar(45) NOT NULL,
  `centerDesc` longtext,
  `centerSize` int NOT NULL,
  `status` varchar(45) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `createdBy` bigint DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `updatedBy` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_center_address_idx` (`addressId`),
  KEY `fk_center_user_createdBy_idx` (`createdBy`),
  KEY `fk_center_user_updatedBy_idx` (`updatedBy`),
  CONSTRAINT `fk_center_address` FOREIGN KEY (`addressId`) REFERENCES `address` (`id`),
  CONSTRAINT `fk_center_user_createdBy` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_center_user_updatedBy` FOREIGN KEY (`updatedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `center`
--

LOCK TABLES `center` WRITE;
/*!40000 ALTER TABLE `center` DISABLE KEYS */;
INSERT INTO `center` VALUES (1,1,'TeachSync Doi Can','ENGLISH',NULL,20,'CREATED',NULL,NULL,NULL,NULL),(2,4,'TeachSync Mai Dich','ENGLISH',NULL,25,'CREATED',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `center` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `certificate`
--

DROP TABLE IF EXISTS `certificate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `certificate` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `courseId` bigint NOT NULL,
  `certificateName` varchar(45) NOT NULL,
  `certificateDesc` longtext,
  `certificateImg` longtext,
  `certificateLink` longtext,
  `certificateContent` mediumblob,
  `status` varchar(45) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `createdBy` bigint DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `updatedBy` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_certificate_user_createdBy_idx` (`createdBy`),
  KEY `fk_certificate_user_updatedBy_idx` (`updatedBy`),
  KEY `fk_certificate_course_idx` (`courseId`),
  CONSTRAINT `fk_certificate_course` FOREIGN KEY (`courseId`) REFERENCES `course` (`id`),
  CONSTRAINT `fk_certificate_user_createdBy` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_certificate_user_updatedBy` FOREIGN KEY (`updatedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `certificate`
--

LOCK TABLES `certificate` WRITE;
/*!40000 ALTER TABLE `certificate` DISABLE KEYS */;
INSERT INTO `certificate` VALUES (1,1,'Ielts 101','You have complete the Ielts 101 course',NULL,NULL,NULL,'CREATED',NULL,NULL,NULL,NULL),(2,2,'Eng 1','You have complete the Eng 1 course',NULL,NULL,NULL,'CREATED',NULL,NULL,NULL,NULL),(3,3,'Eng 2','You have complete the Eng 2 course',NULL,NULL,NULL,'CREATED',NULL,NULL,NULL,NULL),(4,4,'Eng 3','You have complete the Eng 3 course',NULL,NULL,NULL,'CREATED',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `certificate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `certificate_issue`
--

DROP TABLE IF EXISTS `certificate_issue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `certificate_issue` (
  `id` bigint NOT NULL,
  `receiverId` bigint NOT NULL,
  `certificateId` bigint NOT NULL,
  `semesterId` bigint NOT NULL,
  `signatoryId` bigint NOT NULL,
  `presenterId` bigint NOT NULL,
  `issuedAt` datetime DEFAULT NULL,
  `printedAt` datetime DEFAULT NULL,
  `status` varchar(45) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `createdBy` bigint DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `updatedBy` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_certificate_issue_certificate_idx` (`certificateId`),
  KEY `fk_certificate_issue_user_receiverId_idx` (`receiverId`),
  KEY `fk_certificate_issue_semester_idx` (`semesterId`) /*!80000 INVISIBLE */,
  KEY `fk_certificate_issue_user_createdBy_idx` (`createdBy`),
  KEY `fk_certificate_issue_user_updatedBy_idx` (`updatedBy`),
  KEY `fk_certificate_issue_user_signatoryId_idx` (`signatoryId`),
  KEY `fk_certificate_issue_user_presenterId_idx` (`presenterId`),
  CONSTRAINT `fk_certificate_issue_certificate` FOREIGN KEY (`certificateId`) REFERENCES `certificate` (`id`),
  CONSTRAINT `fk_certificate_issue_semester` FOREIGN KEY (`semesterId`) REFERENCES `semester` (`id`),
  CONSTRAINT `fk_certificate_issue_user_createdBy` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_certificate_issue_user_presenterId` FOREIGN KEY (`presenterId`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_certificate_issue_user_receiverId` FOREIGN KEY (`receiverId`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_certificate_issue_user_signatoryId` FOREIGN KEY (`signatoryId`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_certificate_issue_user_updatedBy` FOREIGN KEY (`updatedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `certificate_issue`
--

LOCK TABLES `certificate_issue` WRITE;
/*!40000 ALTER TABLE `certificate_issue` DISABLE KEYS */;
/*!40000 ALTER TABLE `certificate_issue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clazz`
--

DROP TABLE IF EXISTS `clazz`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clazz` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `courseSemesterId` bigint NOT NULL,
  `staffId` bigint NOT NULL,
  `clazzName` varchar(45) NOT NULL,
  `clazzDesc` longtext,
  `clazzSize` int NOT NULL COMMENT 'no of people',
  `status` varchar(45) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `createdBy` bigint DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `updatedBy` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_clazz_course_semester_idx` (`courseSemesterId`),
  KEY `fk_clazz_staff_idx` (`staffId`),
  KEY `fk_clazz_user_createdBy_idx` (`createdBy`),
  KEY `fk_clazz_user_updatedBy_idx` (`updatedBy`),
  CONSTRAINT `fk_clazz_course_semester` FOREIGN KEY (`courseSemesterId`) REFERENCES `course_semester` (`id`),
  CONSTRAINT `fk_clazz_staff` FOREIGN KEY (`staffId`) REFERENCES `staff` (`id`),
  CONSTRAINT `fk_clazz_user_createdBy` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_clazz_user_updatedBy` FOREIGN KEY (`updatedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clazz`
--

LOCK TABLES `clazz` WRITE;
/*!40000 ALTER TABLE `clazz` DISABLE KEYS */;
INSERT INTO `clazz` VALUES (1,1,1,'Class 1 Ielts 101',NULL,15,'CREATED',NULL,NULL,NULL,NULL),(2,2,1,'Class 2 Ielts 101',NULL,15,'CREATED',NULL,NULL,NULL,NULL),(3,5,1,'Class 1 Eng gr 1',NULL,30,'CREATED',NULL,NULL,NULL,NULL),(4,6,1,'Class 2 Eng gr 1',NULL,30,'CREATED',NULL,NULL,NULL,NULL),(5,9,2,'Class 1 Eng gr 2',NULL,30,'CREATED',NULL,NULL,NULL,NULL),(6,10,2,'Class 2 Eng gr 2',NULL,30,'CREATED',NULL,NULL,NULL,NULL),(7,13,2,'Class 1 Eng gr 3',NULL,25,'CREATED',NULL,NULL,NULL,NULL),(8,14,2,'Class 2 Eng gr 3',NULL,25,'CREATED',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `clazz` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clazz_member`
--

DROP TABLE IF EXISTS `clazz_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clazz_member` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `clazzId` bigint NOT NULL,
  `userId` bigint NOT NULL,
  `score` float DEFAULT NULL,
  `attendant` float DEFAULT NULL,
  `isPassed` bit(1) DEFAULT NULL,
  `status` varchar(45) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `createdBy` bigint DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `updatedBy` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_clazz_member_clazz_idx` (`clazzId`),
  KEY `fk_clazz_member_user_idx` (`userId`),
  KEY `fk_clazz_member_user_createdBy_idx` (`createdBy`),
  KEY `fk_clazz_member_user_updatedBy_idx` (`updatedBy`),
  CONSTRAINT `fk_clazz_member_clazz` FOREIGN KEY (`clazzId`) REFERENCES `clazz` (`id`),
  CONSTRAINT `fk_clazz_member_user` FOREIGN KEY (`userId`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_clazz_member_user_createdBy` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_clazz_member_user_updatedBy` FOREIGN KEY (`updatedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clazz_member`
--

LOCK TABLES `clazz_member` WRITE;
/*!40000 ALTER TABLE `clazz_member` DISABLE KEYS */;
/*!40000 ALTER TABLE `clazz_member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clazz_schedule`
--

DROP TABLE IF EXISTS `clazz_schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clazz_schedule` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `clazzId` bigint NOT NULL,
  `roomId` bigint NOT NULL COMMENT 'default room',
  `scheduleType` varchar(45) NOT NULL,
  `startDate` date NOT NULL,
  `endDate` date NOT NULL,
  `slot` int DEFAULT NULL,
  `sessionStart` time NOT NULL,
  `sessionEnd` time NOT NULL,
  `status` varchar(45) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `createdBy` bigint DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `updatedBy` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_clazz_schedule_clazz_idx` (`clazzId`) /*!80000 INVISIBLE */,
  KEY `fk_clazz_schedule_room_idx` (`roomId`),
  KEY `fk_clazz_schedule_user_createdBy_idx` (`createdBy`),
  KEY `fk_clazz_schedule_user_updatedBy_idx` (`updatedBy`),
  CONSTRAINT `fk_clazz_schedule_clazz` FOREIGN KEY (`clazzId`) REFERENCES `clazz` (`id`),
  CONSTRAINT `fk_clazz_schedule_room` FOREIGN KEY (`roomId`) REFERENCES `room` (`id`),
  CONSTRAINT `fk_clazz_schedule_user_createdBy` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_clazz_schedule_user_updatedBy` FOREIGN KEY (`updatedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clazz_schedule`
--

LOCK TABLES `clazz_schedule` WRITE;
/*!40000 ALTER TABLE `clazz_schedule` DISABLE KEYS */;
INSERT INTO `clazz_schedule` VALUES (1,1,1,'MON_WED_FRI','2023-06-10','2023-08-10',1,'07:00:00','08:30:00','CREATED',NULL,NULL,NULL,NULL),(2,2,3,'TUE_THU_SAT','2023-06-10','2023-08-10',3,'10:30:00','12:00:00','CREATED',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `clazz_schedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clazz_test`
--

DROP TABLE IF EXISTS `clazz_test`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clazz_test` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `clazzId` bigint NOT NULL,
  `testId` bigint NOT NULL,
  `openFrom` datetime DEFAULT NULL,
  `openTo` datetime DEFAULT NULL,
  `status` varchar(45) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `createdBy` bigint DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `updatedBy` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_clazz_test_user_updatedBy_idx` (`updatedBy`),
  KEY `fk_clazz_test_user_createdBy_idx` (`createdBy`),
  KEY `fk_clazz_test_test_idx` (`testId`),
  KEY `fk_clazz_test_clazz_idx` (`clazzId`),
  CONSTRAINT `fk_clazz_test_clazz` FOREIGN KEY (`clazzId`) REFERENCES `clazz` (`id`),
  CONSTRAINT `fk_clazz_test_test` FOREIGN KEY (`testId`) REFERENCES `test` (`id`),
  CONSTRAINT `fk_clazz_test_user_createdBy` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_clazz_test_user_updatedBy` FOREIGN KEY (`updatedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clazz_test`
--

LOCK TABLES `clazz_test` WRITE;
/*!40000 ALTER TABLE `clazz_test` DISABLE KEYS */;
/*!40000 ALTER TABLE `clazz_test` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course`
--

DROP TABLE IF EXISTS `course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `courseName` varchar(45) NOT NULL,
  `courseAlias` varchar(10) NOT NULL,
  `courseImg` longtext NOT NULL,
  `courseDesc` longtext,
  `numSession` int NOT NULL,
  `minScore` float NOT NULL COMMENT 'Min total score needed to pass',
  `minAttendant` float NOT NULL COMMENT 'Min % of total attendant needed to pass',
  `status` varchar(45) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `createdBy` bigint DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `updatedBy` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_course_user_createdBy_idx` (`createdBy`),
  KEY `fk_course_user_updatedBy_idx` (`updatedBy`),
  CONSTRAINT `fk_course_user_createdBy` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_course_user_updatedBy` FOREIGN KEY (`updatedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course`
--

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course` DISABLE KEYS */;
INSERT INTO `course` VALUES (1,'ielts 101','ILT101','https://th.bing.com/th/id/OIP.R7Wj-CVruj2Gcx-MmaxmZAHaKe?pid=ImgDet&rs=1',NULL,14,5,80,'CREATED',NULL,NULL,NULL,NULL),(2,'English Grade 1','ENG001','https://th.bing.com/th/id/OIP.isjamm3juJANzM_sHKCx0wHaKe?pid=ImgDet&rs=1',NULL,12,4.5,75,'CREATED',NULL,NULL,NULL,NULL),(3,'English Grade 2','ENG002','https://www.geo.tv/assets/uploads/updates/2021-05-03/348462_790220_updates.jpg',NULL,12,5,75,'CREATED',NULL,NULL,NULL,NULL),(4,'English Grade 3','ENG003','https://th.bing.com/th/id/OIP.H1Q3_d4okF70VIVYp-YA5AAAAA?pid=ImgDet&w=360&h=470&rs=1',NULL,12,5,75,'CREATED',NULL,NULL,NULL,NULL),(5,'a','a','https://firebasestorage.googleapis.com/v0/b/teachsync.appspot.com/o/1690380574548-1f28b5e0b08263dc3a93.jpg?alt=media&token=492a305c-7143-4671-b62e-4aa1c307c4c1','a',10,5,80,'CREATED','2023-07-26 21:09:37',4,'2023-07-26 21:09:37',NULL);
/*!40000 ALTER TABLE `course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course_material`
--

DROP TABLE IF EXISTS `course_material`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course_material` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `courseId` bigint NOT NULL,
  `materialId` bigint NOT NULL,
  `status` varchar(45) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `createdBy` bigint DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `updatedBy` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_course_material_material_idx` (`materialId`),
  KEY `fk_course_material_course_idx` (`courseId`),
  KEY `fk_course_material_user_createdBy_idx` (`createdBy`),
  KEY `fk_course_material_user_updatedBy_idx` (`updatedBy`),
  CONSTRAINT `fk_course_material_course` FOREIGN KEY (`courseId`) REFERENCES `course` (`id`),
  CONSTRAINT `fk_course_material_material` FOREIGN KEY (`materialId`) REFERENCES `material` (`id`),
  CONSTRAINT `fk_course_material_user_createdBy` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_course_material_user_updatedBy` FOREIGN KEY (`updatedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course_material`
--

LOCK TABLES `course_material` WRITE;
/*!40000 ALTER TABLE `course_material` DISABLE KEYS */;
/*!40000 ALTER TABLE `course_material` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course_semester`
--

DROP TABLE IF EXISTS `course_semester`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course_semester` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `courseId` bigint NOT NULL,
  `semesterId` bigint NOT NULL,
  `centerId` bigint NOT NULL,
  `status` varchar(45) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `createdBy` bigint DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `updatedBy` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_course_semester_semester_idx` (`semesterId`),
  KEY `fk_course_semester_center_idx` (`centerId`),
  KEY `fk_course_semester_course_idx` (`courseId`),
  KEY `fk_course_semester_user_createdBy_idx` (`createdBy`),
  KEY `fk_course_semester_user_updatedBy_idx` (`updatedBy`),
  CONSTRAINT `fk_course_semester_center` FOREIGN KEY (`centerId`) REFERENCES `center` (`id`),
  CONSTRAINT `fk_course_semester_course` FOREIGN KEY (`courseId`) REFERENCES `course` (`id`),
  CONSTRAINT `fk_course_semester_semester` FOREIGN KEY (`semesterId`) REFERENCES `semester` (`id`),
  CONSTRAINT `fk_course_semester_user_createdBy` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_course_semester_user_updatedBy` FOREIGN KEY (`updatedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course_semester`
--

LOCK TABLES `course_semester` WRITE;
/*!40000 ALTER TABLE `course_semester` DISABLE KEYS */;
INSERT INTO `course_semester` VALUES (1,1,1,1,'CREATED',NULL,NULL,NULL,NULL),(2,1,2,1,'CREATED',NULL,NULL,NULL,NULL),(3,1,3,1,'CREATED',NULL,NULL,NULL,NULL),(4,1,4,1,'CREATED',NULL,NULL,NULL,NULL),(5,2,1,1,'CREATED',NULL,NULL,NULL,NULL),(6,2,2,1,'CREATED',NULL,NULL,NULL,NULL),(7,2,3,1,'CREATED',NULL,NULL,NULL,NULL),(8,2,4,1,'CREATED',NULL,NULL,NULL,NULL),(9,3,1,1,'CREATED',NULL,NULL,NULL,NULL),(10,3,2,1,'CREATED',NULL,NULL,NULL,NULL),(11,3,3,1,'CREATED',NULL,NULL,NULL,NULL),(12,3,4,1,'CREATED',NULL,NULL,NULL,NULL),(13,4,1,1,'CREATED',NULL,NULL,NULL,NULL),(14,4,2,1,'CREATED',NULL,NULL,NULL,NULL),(15,4,3,1,'CREATED',NULL,NULL,NULL,NULL),(16,1,1,2,'CREATED',NULL,NULL,NULL,NULL),(17,1,2,2,'CREATED',NULL,NULL,NULL,NULL),(18,1,3,2,'CREATED',NULL,NULL,NULL,NULL),(19,1,4,2,'CREATED',NULL,NULL,NULL,NULL),(20,2,1,2,'CREATED',NULL,NULL,NULL,NULL),(21,2,2,2,'CREATED',NULL,NULL,NULL,NULL),(22,2,3,2,'CREATED',NULL,NULL,NULL,NULL),(23,2,4,2,'CREATED',NULL,NULL,NULL,NULL),(24,3,1,2,'CREATED',NULL,NULL,NULL,NULL),(25,3,2,2,'CREATED',NULL,NULL,NULL,NULL),(26,3,3,2,'CREATED',NULL,NULL,NULL,NULL),(27,3,4,2,'CREATED',NULL,NULL,NULL,NULL),(28,4,1,2,'CREATED',NULL,NULL,NULL,NULL),(29,4,2,2,'CREATED',NULL,NULL,NULL,NULL),(30,4,3,2,'CREATED',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `course_semester` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `homework`
--

DROP TABLE IF EXISTS `homework`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `homework` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `clazzId` bigint NOT NULL,
  `homeworkName` varchar(45) NOT NULL,
  `homeworkDesc` longtext,
  `homeworkDoc` longtext,
  `homeworkContent` mediumblob,
  `openAt` datetime DEFAULT NULL,
  `deadline` datetime DEFAULT NULL,
  `status` varchar(45) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `createdBy` bigint DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `updatedBy` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_homework_clazz_idx` (`clazzId`),
  KEY `fk_homework_user_createdBy_idx` (`createdBy`),
  KEY `fk_homework_user_updatedBy_idx` (`updatedBy`),
  CONSTRAINT `fk_homework_clazz` FOREIGN KEY (`clazzId`) REFERENCES `clazz` (`id`),
  CONSTRAINT `fk_homework_user_createdBy` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_homework_user_updatedBy` FOREIGN KEY (`updatedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `homework`
--

LOCK TABLES `homework` WRITE;
/*!40000 ALTER TABLE `homework` DISABLE KEYS */;
INSERT INTO `homework` VALUES (1,1,'Mr D 123','Mr D 123','df',NULL,'2023-07-06 22:46:00','2023-07-30 19:50:00','UPDATED','2023-07-24 19:46:30',3,'2023-07-24 20:01:58',3),(2,1,'demo bt 1','demo bt 1',NULL,NULL,'2023-07-05 20:00:00','2023-07-30 20:00:00','CREATED','2023-07-24 20:00:27',3,'2023-07-24 20:00:27',3);
/*!40000 ALTER TABLE `homework` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `location_unit`
--

DROP TABLE IF EXISTS `location_unit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `location_unit` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `parentId` bigint DEFAULT NULL,
  `level` int NOT NULL,
  `unitName` varchar(255) NOT NULL,
  `unitAlias` varchar(45) NOT NULL,
  `unitType` varchar(45) NOT NULL,
  `status` varchar(45) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `createdBy` bigint DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `updatedBy` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_location_unit_location_unit_parentId_idx` (`parentId`),
  KEY `fk_location_unit_user_createdBy_idx` (`createdBy`),
  KEY `fk_location_unit_user_updatedBy_idx` (`updatedBy`),
  CONSTRAINT `fk_location_unit_location_unit_parentId` FOREIGN KEY (`parentId`) REFERENCES `location_unit` (`id`),
  CONSTRAINT `fk_location_unit_user_createdBy` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_location_unit_user_updatedBy` FOREIGN KEY (`updatedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `location_unit`
--

LOCK TABLES `location_unit` WRITE;
/*!40000 ALTER TABLE `location_unit` DISABLE KEYS */;
INSERT INTO `location_unit` VALUES (1,NULL,0,'Vietnam','VN','COUNTRY','CREATED',NULL,NULL,NULL,NULL),(2,1,1,'Hanoi','TP.HN','MUNICIPALITY','CREATED',NULL,NULL,NULL,NULL),(3,1,1,'Ho Chi Minh','TP.HCM','MUNICIPALITY','CREATED',NULL,NULL,NULL,NULL),(4,2,2,'Ba Dinh','Q.Ba Dinh','DISTRICT','CREATED',NULL,NULL,NULL,NULL),(5,2,2,'Cau Giay','Q.Cau Giay','DISTRICT','CREATED',NULL,NULL,NULL,NULL),(6,3,2,'1','Q.1','DISTRICT','CREATED',NULL,NULL,NULL,NULL),(7,3,2,'3','Q.3','DISTRICT','CREATED',NULL,NULL,NULL,NULL),(8,4,3,'Doi Can','P.Doi Can','WARD','CREATED',NULL,NULL,NULL,NULL),(9,4,3,'Ngoc Ha','P.Ngoc Ha','WARD','CREATED',NULL,NULL,NULL,NULL),(10,5,3,'Dich Vong','P.Dich Vong','WARD','CREATED',NULL,NULL,NULL,NULL),(11,5,3,'Mai Dich','P.Mai Dich','WARD','CREATED',NULL,NULL,NULL,NULL),(12,6,3,'Ben Thanh','P.Ben Thanh','WARD','CREATED',NULL,NULL,NULL,NULL),(13,6,3,'Tan Dinh','P.Tan Dinh','WARD','CREATED',NULL,NULL,NULL,NULL),(14,7,3,'1','P.1','WARD','CREATED',NULL,NULL,NULL,NULL),(15,7,3,'2','P.2','WARD','CREATED',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `location_unit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `material`
--

DROP TABLE IF EXISTS `material`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `material` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `materialName` varchar(45) NOT NULL,
  `materialImg` longtext NOT NULL,
  `materialLink` longtext,
  `materialContent` mediumblob,
  `materialType` varchar(45) NOT NULL,
  `isFree` bit(1) NOT NULL DEFAULT b'1' COMMENT 'can it be view by guest?',
  `status` varchar(45) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `createdBy` bigint DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `updatedBy` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_material_user_createdBy_idx` (`createdBy`),
  KEY `fk_material_user_updatedBy_idx` (`updatedBy`),
  CONSTRAINT `fk_material_user_createdBy` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_material_user_updatedBy` FOREIGN KEY (`updatedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `material`
--

LOCK TABLES `material` WRITE;
/*!40000 ALTER TABLE `material` DISABLE KEYS */;
/*!40000 ALTER TABLE `material` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `member_homework_record`
--

DROP TABLE IF EXISTS `member_homework_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member_homework_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `memberId` bigint NOT NULL,
  `homeworkId` bigint NOT NULL,
  `submission` longtext COMMENT 'file, 16.76 Mb',
  `submissionLink` longtext,
  `score` float DEFAULT NULL COMMENT 'max 10.00',
  `status` varchar(45) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `createdBy` bigint DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `updatedBy` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_member_homework_record_clazz_member_idx` (`memberId`),
  KEY `fk_member_homework_record_homework_idx` (`homeworkId`),
  KEY `fk_member_homework_record_user_createdBy_idx` (`createdBy`),
  KEY `fk_member_homework_record_user_updatedBy_idx` (`updatedBy`),
  CONSTRAINT `fk_member_homework_record_homework` FOREIGN KEY (`homeworkId`) REFERENCES `homework` (`id`),
  CONSTRAINT `fk_member_homework_record_user_createdBy` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_member_homework_record_user_updatedBy` FOREIGN KEY (`updatedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member_homework_record`
--

LOCK TABLES `member_homework_record` WRITE;
/*!40000 ALTER TABLE `member_homework_record` DISABLE KEYS */;
INSERT INTO `member_homework_record` VALUES (5,'Bài tập - 25-07-2023 18:59:08 - duong',7,1,NULL,'',NULL,'CREATED','2023-07-25 18:59:08',7,'2023-07-25 18:59:08',7),(6,'Bài tập - 25-07-2023 18:59:12 - duong',7,1,NULL,'',NULL,'CREATED','2023-07-25 18:59:12',7,'2023-07-25 18:59:12',7),(7,'Bài tập - 25-07-2023 19:01:21 - Test Student',1,1,NULL,'',NULL,'CREATED','2023-07-25 19:01:21',1,'2023-07-25 19:01:21',1),(8,'Bài tập - 25-07-2023 19:01:24 - Test Student',1,1,NULL,'',NULL,'DELETED','2023-07-25 19:01:25',1,'2023-07-25 19:33:48',1),(9,'Bài tập - 25-07-2023 19:21:16 - duong',7,1,NULL,'https://www.youtube.com/watch?v=gIgI7ZO9yTw&list=TLPQMjUwNzIwMjMk1kX_xqEIhg&index=12',NULL,'CREATED','2023-07-25 19:21:16',7,'2023-07-25 19:21:16',7),(10,'Bài tập - 25-07-2023 19:21:35 - duong',7,1,NULL,NULL,NULL,'DELETED','2023-07-25 19:21:35',7,'2023-07-25 19:33:33',7),(11,'Bài tập - 26-07-2023 21:19:22 - duong',7,1,'https://firebasestorage.googleapis.com/v0/b/teachsync.appspot.com/o/1690381158810-1f28b5e0b08263dc3a93.jpg?alt=media&token=99ad552e-2a75-4a4c-a85f-8b9b4196f5a5','',NULL,'DELETED','2023-07-26 21:19:22',7,'2023-07-26 21:23:00',7),(12,'Bài tập - 26-07-2023 21:22:37 - duong',7,1,'https://firebasestorage.googleapis.com/v0/b/teachsync.appspot.com/o/1690381356900-sdf.txt?alt=media&token=497deef1-4679-43fa-b3de-88dff9d9d085','',NULL,'DELETED','2023-07-26 21:22:38',7,'2023-07-26 21:22:58',7),(13,'Bài tập - 26-07-2023 21:23:04 - duong',7,1,'https://firebasestorage.googleapis.com/v0/b/teachsync.appspot.com/o/1690381383791-sdf.txt?alt=media&token=bdb452b6-8fa7-4583-a37e-1f905e80a1f8','',NULL,'CREATED','2023-07-26 21:23:05',7,'2023-07-26 21:23:05',7),(14,'Bài tập - 26-07-2023 21:29:46 - duong',7,1,'https://firebasestorage.googleapis.com/v0/b/teachsync.appspot.com/o/1690381776444-lan.png?alt=media&token=980e2835-75fb-47de-8761-cd2b08ffec49','',NULL,'CREATED','2023-07-26 21:29:46',7,'2023-07-26 21:29:46',7);
/*!40000 ALTER TABLE `member_homework_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `member_test_record`
--

DROP TABLE IF EXISTS `member_test_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member_test_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `memberId` bigint NOT NULL,
  `clazzTestId` bigint NOT NULL,
  `score` double DEFAULT NULL,
  `status` varchar(45) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `createdBy` bigint DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `updatedBy` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_member_test_record_clazz_member_idx` (`memberId`),
  KEY `fk_member_test_record_clazz_test_idx` (`clazzTestId`),
  KEY `fk_member_test_record_user_createdBy_idx` (`createdBy`),
  KEY `fk_member_test_record_user_updatedBy_idx` (`updatedBy`),
  CONSTRAINT `fk_member_test_record_clazz_member` FOREIGN KEY (`memberId`) REFERENCES `clazz_member` (`id`),
  CONSTRAINT `fk_member_test_record_clazz_test` FOREIGN KEY (`clazzTestId`) REFERENCES `clazz_test` (`id`),
  CONSTRAINT `fk_member_test_record_user_createdBy` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_member_test_record_user_updatedBy` FOREIGN KEY (`updatedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member_test_record`
--

LOCK TABLES `member_test_record` WRITE;
/*!40000 ALTER TABLE `member_test_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `member_test_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `news`
--

DROP TABLE IF EXISTS `news`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `news` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `authorId` bigint NOT NULL,
  `newsTitle` varchar(45) NOT NULL,
  `newsContent` mediumblob,
  `newsLink` longtext,
  `newsDesc` longtext,
  `status` varchar(45) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `createdBy` bigint DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `updatedBy` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_news_user_authorId_idx` (`authorId`),
  KEY `fk_news_user_createdBy_idx` (`createdBy`),
  KEY `fk_news_user_updatedBy_idx` (`updatedBy`),
  CONSTRAINT `fk_news_user_authorId` FOREIGN KEY (`authorId`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_news_user_createdBy` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_news_user_updatedBy` FOREIGN KEY (`updatedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `news`
--

LOCK TABLES `news` WRITE;
/*!40000 ALTER TABLE `news` DISABLE KEYS */;
/*!40000 ALTER TABLE `news` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment`
--

DROP TABLE IF EXISTS `payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `payerId` bigint NOT NULL,
  `requestId` bigint NOT NULL,
  `paymentType` varchar(45) NOT NULL,
  `paymentAmount` double NOT NULL,
  `paymentAt` datetime NOT NULL,
  `paymentDoc` mediumblob,
  `paymentDocLink` longtext,
  `verifierId` bigint NOT NULL,
  `status` varchar(45) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `createdBy` bigint DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `updatedBy` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_payment_request_idx` (`requestId`),
  KEY `fk_payment_user_payerId_idx` (`payerId`),
  KEY `fk_payment_user_verifierId_idx` (`verifierId`),
  KEY `fk_payment_user_createdBy_idx` (`createdBy`),
  KEY `fk_payment_user_updatedBy_idx` (`updatedBy`),
  CONSTRAINT `fk_payment_request` FOREIGN KEY (`requestId`) REFERENCES `request` (`id`),
  CONSTRAINT `fk_payment_user_createdBy` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_payment_user_payerId` FOREIGN KEY (`payerId`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_payment_user_updatedBy` FOREIGN KEY (`updatedBy`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_payment_user_verifierId` FOREIGN KEY (`verifierId`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment`
--

LOCK TABLES `payment` WRITE;
/*!40000 ALTER TABLE `payment` DISABLE KEYS */;
/*!40000 ALTER TABLE `payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `price_log`
--

DROP TABLE IF EXISTS `price_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `price_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `courseId` bigint NOT NULL,
  `price` double NOT NULL,
  `isPromotion` bit(1) NOT NULL,
  `promotionAmount` double DEFAULT NULL,
  `promotionType` varchar(45) DEFAULT NULL,
  `promotionDesc` longtext,
  `validFrom` datetime NOT NULL,
  `validTo` datetime DEFAULT NULL,
  `status` varchar(45) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `createdBy` bigint DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `updatedBy` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_price_log_course_idx` (`courseId`),
  KEY `fk_price_log_user_createdBy_idx` (`createdBy`),
  KEY `fk_price_log_user_updatedBy_idx` (`updatedBy`),
  CONSTRAINT `fk_price_log_course` FOREIGN KEY (`courseId`) REFERENCES `course` (`id`),
  CONSTRAINT `fk_price_log_user_createdBy` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_price_log_user_updatedBy` FOREIGN KEY (`updatedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `price_log`
--

LOCK TABLES `price_log` WRITE;
/*!40000 ALTER TABLE `price_log` DISABLE KEYS */;
INSERT INTO `price_log` VALUES (1,1,200000,_binary '',15,'PERCENT','Grand opening super deal, now cheaper ','2023-06-10 00:00:01',NULL,'CREATED',NULL,NULL,NULL,NULL),(2,2,100000,_binary '',20000,'AMOUNT','To help with the first step, now reduce','2023-06-10 00:00:01',NULL,'CREATED',NULL,NULL,NULL,NULL),(3,3,125000,_binary '\0',NULL,NULL,NULL,'2023-06-10 00:00:01',NULL,'CREATED',NULL,NULL,NULL,NULL),(4,4,150000,_binary '\0',NULL,NULL,NULL,'2023-06-10 00:00:01',NULL,'CREATED',NULL,NULL,NULL,NULL),(5,5,100000,_binary '\0',NULL,NULL,NULL,'2023-07-26 21:09:37',NULL,'CREATED','2023-07-26 21:09:37',NULL,'2023-07-26 21:09:37',NULL);
/*!40000 ALTER TABLE `price_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `testId` bigint NOT NULL,
  `questionType` varchar(255) NOT NULL,
  `questionDesc` longtext NOT NULL,
  `questionPrompt` varchar(45) NOT NULL,
  `status` varchar(45) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `createdBy` bigint DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `updatedBy` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_question_test_idx` (`testId`),
  KEY `fk_question_user_createdBy_idx` (`createdBy`),
  KEY `fk_question_user_updatedBy_idx` (`updatedBy`),
  CONSTRAINT `fk_question_test` FOREIGN KEY (`testId`) REFERENCES `test` (`id`),
  CONSTRAINT `fk_question_user_createdBy` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_question_user_updatedBy` FOREIGN KEY (`updatedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recruitment_campaign`
--

DROP TABLE IF EXISTS `recruitment_campaign`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recruitment_campaign` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `centerId` bigint NOT NULL,
  `campaignImg` longtext NOT NULL,
  `campaignName` varchar(45) NOT NULL,
  `campaignDesc` longtext,
  `position` varchar(45) NOT NULL,
  `openingSlot` int NOT NULL COMMENT 'No of applicant expect to be accepted',
  `recruitFrom` datetime NOT NULL,
  `recruitTo` datetime NOT NULL,
  `status` varchar(45) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `createdBy` bigint DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `updatedBy` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_recruitment_campaign_center_idx` (`centerId`),
  KEY `fk_recruitment_campaign_user_createdBy_idx` (`createdBy`),
  KEY `fk_recruitment_campaign_user_updatedBy_idx` (`updatedBy`),
  CONSTRAINT `fk_campaign_center` FOREIGN KEY (`centerId`) REFERENCES `center` (`id`),
  CONSTRAINT `fk_recruitment_campaign_user_createdBy` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_recruitment_campaign_user_updatedBy` FOREIGN KEY (`updatedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recruitment_campaign`
--

LOCK TABLES `recruitment_campaign` WRITE;
/*!40000 ALTER TABLE `recruitment_campaign` DISABLE KEYS */;
INSERT INTO `recruitment_campaign` VALUES (1,1,'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRdLpM2NuHCmmYjvWgq1W9A3P8X4UjHov04CQbKOZdWDG31Adky365iY45ABveL_sMj_vI&usqp=CAU','Mr D 123','2','2',2,'2023-07-17 22:58:00','2023-07-17 22:59:00','CREATED','2023-07-17 22:56:17',4,'2023-07-17 22:56:17',4),(2,2,'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRdLpM2NuHCmmYjvWgq1W9A3P8X4UjHov04CQbKOZdWDG31Adky365iY45ABveL_sMj_vI&usqp=CAU','Mr D 123','2','2',2,'2023-07-17 22:58:00','2023-07-17 22:59:00','CREATED','2023-07-17 23:24:37',4,'2023-07-17 23:24:37',4),(3,1,'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRdLpM2NuHCmmYjvWgq1W9A3P8X4UjHov04CQbKOZdWDG31Adky365iY45ABveL_sMj_vI&usqp=CAU','demo123','demo','demo',2,'2023-07-17 23:30:00','2023-07-17 23:30:00','DELETED','2023-07-17 23:27:36',4,'2023-07-17 23:27:53',4),(4,1,'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRdLpM2NuHCmmYjvWgq1W9A3P8X4UjHov04CQbKOZdWDG31Adky365iY45ABveL_sMj_vI&usqp=CAU','demo messsage','demo messsage','demo messsage',2,'2023-07-17 14:28:00','2023-07-17 23:31:00','CREATED','2023-07-17 23:28:40',4,'2023-07-17 23:28:40',4);
/*!40000 ALTER TABLE `recruitment_campaign` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `request`
--

DROP TABLE IF EXISTS `request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `request` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `requesterId` bigint NOT NULL,
  `requestName` varchar(45) NOT NULL,
  `requestDesc` longtext NOT NULL,
  `requestType` varchar(45) NOT NULL,
  `clazzId` bigint DEFAULT NULL,
  `requestContent` mediumblob,
  `contentLink` longtext,
  `resolverId` bigint DEFAULT NULL,
  `status` varchar(45) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `createdBy` bigint DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `updatedBy` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_request_user_requesterId_idx` (`requesterId`),
  KEY `fk_request_user_resolverId_idx` (`resolverId`),
  KEY `fk_request_user_createdBy_idx` (`createdBy`),
  KEY `fk_request_user_updatedBy_idx` (`updatedBy`),
  KEY `fk_request_clazz_idx` (`clazzId`),
  CONSTRAINT `fk_request_clazz` FOREIGN KEY (`clazzId`) REFERENCES `clazz` (`id`),
  CONSTRAINT `fk_request_user_createdBy` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_request_user_requesterId` FOREIGN KEY (`requesterId`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_request_user_resolverId` FOREIGN KEY (`resolverId`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_request_user_updatedBy` FOREIGN KEY (`updatedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `request`
--

LOCK TABLES `request` WRITE;
/*!40000 ALTER TABLE `request` DISABLE KEYS */;
/*!40000 ALTER TABLE `request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `roleName` varchar(45) NOT NULL,
  `roleDesc` longtext,
  `status` varchar(45) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `createdBy` bigint DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `updatedBy` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_role_user_createdBy_idx` (`createdBy`),
  KEY `fk_role_user_updatedBy_idx` (`updatedBy`),
  CONSTRAINT `fk_role_user_createdBy` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_role_user_updatedBy` FOREIGN KEY (`updatedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'Student','Student','CREATED',NULL,NULL,NULL,NULL),(2,'Parent','Parent','CREATED',NULL,NULL,NULL,NULL),(3,'Teacher','Teacher','CREATED',NULL,NULL,NULL,NULL),(4,'Admin','Admin','CREATED',NULL,NULL,NULL,NULL),(5,'Staff','Staff','CREATED',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `centerId` bigint NOT NULL,
  `roomType` varchar(45) DEFAULT NULL,
  `roomDesc` longtext,
  `roomName` varchar(45) DEFAULT NULL,
  `roomSize` int NOT NULL COMMENT 'no of people',
  `status` varchar(45) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `createdBy` bigint DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `updatedBy` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_room_center_idx` (`centerId`),
  KEY `fk_room_user_createdBy_idx` (`createdBy`),
  KEY `fk_room_user_updatedBy_idx` (`updatedBy`),
  CONSTRAINT `fk_room_center` FOREIGN KEY (`centerId`) REFERENCES `center` (`id`),
  CONSTRAINT `fk_room_user_createdBy` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_room_user_updatedBy` FOREIGN KEY (`updatedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
INSERT INTO `room` VALUES (1,1,'CLASSROOM',NULL,'101',30,'CREATED',NULL,NULL,NULL,NULL),(2,1,'CLASSROOM',NULL,'102',20,'CREATED',NULL,NULL,NULL,NULL),(3,1,'CLASSROOM',NULL,'103',20,'CREATED',NULL,NULL,NULL,NULL),(4,1,'CLASSROOM',NULL,'104',40,'CREATED',NULL,NULL,NULL,NULL),(5,1,'CLASSROOM',NULL,'105',30,'CREATED',NULL,NULL,NULL,NULL),(6,2,'CLASSROOM',NULL,'101',25,'CREATED',NULL,NULL,NULL,NULL),(7,2,'CLASSROOM',NULL,'102',25,'CREATED',NULL,NULL,NULL,NULL),(8,2,'CLASSROOM',NULL,'103',25,'CREATED',NULL,NULL,NULL,NULL),(9,2,'CLASSROOM',NULL,'104',25,'CREATED',NULL,NULL,NULL,NULL),(10,2,'CLASSROOM',NULL,'105',20,'CREATED',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `semester`
--

DROP TABLE IF EXISTS `semester`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `semester` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `semesterName` varchar(45) NOT NULL,
  `semesterAlias` varchar(10) NOT NULL,
  `semesterDesc` longtext,
  `semesterType` varchar(45) NOT NULL,
  `startDate` date NOT NULL,
  `endDate` date NOT NULL,
  `status` varchar(45) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `createdBy` bigint DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `updatedBy` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_semester_user_createdBy_idx` (`createdBy`),
  KEY `fk_semester_user_updatedBy_idx` (`updatedBy`),
  CONSTRAINT `fk_semester_user_createdBy` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_semester_user_updatedBy` FOREIGN KEY (`updatedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `semester`
--

LOCK TABLES `semester` WRITE;
/*!40000 ALTER TABLE `semester` DISABLE KEYS */;
INSERT INTO `semester` VALUES (1,'Spring 23','SP23','Spring 23','SEASON','2023-01-05','2023-03-05','CREATED',NULL,NULL,NULL,NULL),(2,'Summer 23','SU23','Summer 23','SEASON','2023-03-10','2023-06-10','CREATED',NULL,NULL,NULL,NULL),(3,'Fall 23','FA23','Fall 23','SEASON','2023-06-15','2023-09-15','CREATED',NULL,NULL,NULL,NULL),(4,'Winter 23','WI23','Winter 23','SEASON','2023-09-15','2023-12-15','CREATED',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `semester` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `session`
--

DROP TABLE IF EXISTS `session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `session` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `roomId` bigint NOT NULL COMMENT 'actual room',
  `scheduleId` bigint NOT NULL,
  `staffId` bigint NOT NULL,
  `slot` int DEFAULT NULL,
  `sessionStart` datetime NOT NULL,
  `sessionEnd` datetime NOT NULL,
  `status` varchar(45) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `createdBy` bigint DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `updatedBy` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_session_room_idx` (`roomId`),
  KEY `fk_session_schedule_idx` (`scheduleId`),
  KEY `fk_session_user_createdBy_idx` (`createdBy`),
  KEY `fk_session_user_updatedBy_idx` (`updatedBy`),
  KEY `fk_session_center_staff_idx` (`staffId`),
  CONSTRAINT `fk_session_center_staff` FOREIGN KEY (`staffId`) REFERENCES `staff` (`id`),
  CONSTRAINT `fk_session_room` FOREIGN KEY (`roomId`) REFERENCES `room` (`id`),
  CONSTRAINT `fk_session_schedule` FOREIGN KEY (`scheduleId`) REFERENCES `clazz_schedule` (`id`),
  CONSTRAINT `fk_session_user_createdBy` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_session_user_updatedBy` FOREIGN KEY (`updatedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `session`
--

LOCK TABLES `session` WRITE;
/*!40000 ALTER TABLE `session` DISABLE KEYS */;
INSERT INTO `session` VALUES (1,1,1,1,1,'2023-06-12 07:00:00','2023-06-12 08:30:00','CREATED',NULL,NULL,NULL,NULL),(2,1,1,1,1,'2023-06-14 07:00:00','2023-06-14 08:30:00','CREATED',NULL,NULL,NULL,NULL),(3,1,1,1,1,'2023-06-16 07:00:00','2023-06-16 08:30:00','CREATED',NULL,NULL,NULL,NULL),(4,1,1,1,1,'2023-06-19 07:00:00','2023-06-19 08:30:00','CREATED',NULL,NULL,NULL,NULL),(5,1,1,1,1,'2023-06-21 07:00:00','2023-06-21 08:30:00','CREATED',NULL,NULL,NULL,NULL),(6,1,1,1,1,'2023-06-23 07:00:00','2023-06-23 08:30:00','CREATED',NULL,NULL,NULL,NULL),(7,1,1,1,1,'2023-06-26 07:00:00','2023-06-26 08:30:00','CREATED',NULL,NULL,NULL,NULL),(8,1,1,1,1,'2023-06-28 07:00:00','2023-06-28 08:30:00','CREATED',NULL,NULL,NULL,NULL),(9,1,1,1,1,'2023-06-30 07:00:00','2023-06-30 08:30:00','CREATED',NULL,NULL,NULL,NULL),(10,1,1,1,1,'2023-07-03 07:00:00','2023-07-03 08:30:00','CREATED',NULL,NULL,NULL,NULL),(11,1,1,1,1,'2023-07-05 07:00:00','2023-07-05 08:30:00','CREATED',NULL,NULL,NULL,NULL),(12,1,1,1,1,'2023-07-07 07:00:00','2023-07-07 08:30:00','CREATED',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `staff`
--

DROP TABLE IF EXISTS `staff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `staff` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `centerId` bigint NOT NULL,
  `userId` bigint NOT NULL,
  `staffType` varchar(45) NOT NULL,
  `status` varchar(45) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `createdBy` bigint DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `updatedBy` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_staff_center_idx` (`centerId`),
  KEY `fk_staff_user_idx` (`userId`),
  KEY `fk_staff_user_createdBy_idx` (`createdBy`),
  KEY `fk_staff_user_updatedBy_idx` (`updatedBy`),
  CONSTRAINT `fk_staff_center` FOREIGN KEY (`centerId`) REFERENCES `center` (`id`),
  CONSTRAINT `fk_staff_user` FOREIGN KEY (`userId`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_staff_user_createdBy` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_staff_user_updatedBy` FOREIGN KEY (`updatedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `staff`
--

LOCK TABLES `staff` WRITE;
/*!40000 ALTER TABLE `staff` DISABLE KEYS */;
INSERT INTO `staff` VALUES (1,1,3,'TEACHER','CREATED',NULL,NULL,NULL,NULL),(2,2,6,'TEACHER','CREATED',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `staff` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test`
--

DROP TABLE IF EXISTS `test`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `test` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `courseId` bigint DEFAULT NULL,
  `testName` varchar(45) NOT NULL,
  `testType` varchar(45) NOT NULL,
  `testImg` longtext,
  `testDesc` longtext,
  `timeLimit` int NOT NULL,
  `numQuestion` int NOT NULL,
  `minScore` float NOT NULL,
  `testWeight` int NOT NULL,
  `totalScore` float DEFAULT NULL,
  `status` varchar(45) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `createdBy` bigint DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `updatedBy` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_test_user_createdBy_idx` (`createdBy`),
  KEY `fk_test_user_updatedBy_idx` (`updatedBy`),
  KEY `fk_test_course_idx` (`courseId`),
  CONSTRAINT `fk_test_course` FOREIGN KEY (`courseId`) REFERENCES `course` (`id`),
  CONSTRAINT `fk_test_user_createdBy` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_test_user_updatedBy` FOREIGN KEY (`updatedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test`
--

LOCK TABLES `test` WRITE;
/*!40000 ALTER TABLE `test` DISABLE KEYS */;
/*!40000 ALTER TABLE `test` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_record`
--

DROP TABLE IF EXISTS `test_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `test_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `memberTestRecordId` bigint NOT NULL,
  `answerId` bigint NOT NULL,
  `answerTxt` longtext,
  `score` float DEFAULT NULL,
  `status` varchar(45) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `createdBy` bigint DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `updatedBy` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_test_record_answer_idx` (`answerId`),
  KEY `fk_test_record_member_test_record` (`memberTestRecordId`),
  KEY `fk_test_record_user_createdBy_idx` (`createdBy`),
  KEY `fk_test_record_user_updatedBy_idx` (`updatedBy`),
  CONSTRAINT `fk_test_record_answer` FOREIGN KEY (`answerId`) REFERENCES `answer` (`id`),
  CONSTRAINT `fk_test_record_member_test_record` FOREIGN KEY (`memberTestRecordId`) REFERENCES `member_test_record` (`id`),
  CONSTRAINT `fk_test_record_user_createdBy` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_test_record_user_updatedBy` FOREIGN KEY (`updatedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_record`
--

LOCK TABLES `test_record` WRITE;
/*!40000 ALTER TABLE `test_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `test_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `password` varchar(255) NOT NULL,
  `roleId` bigint NOT NULL,
  `userAvatar` longtext,
  `fullName` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `phone` varchar(10) DEFAULT NULL,
  `addressId` bigint DEFAULT NULL,
  `resetPasswordToken` varchar(255) DEFAULT NULL,
  `parentId` bigint DEFAULT NULL,
  `status` varchar(45) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `createdBy` bigint DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `updatedBy` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_user_address_idx` (`addressId`),
  KEY `fk_user_user_parentId_idx` (`parentId`),
  KEY `fk_user_role_idx` (`roleId`),
  KEY `fk_user_user_createdBy_idx` (`createdBy`),
  KEY `fk_user_user_updatedBy_idx` (`updatedBy`),
  CONSTRAINT `fk_user_address` FOREIGN KEY (`addressId`) REFERENCES `address` (`id`),
  CONSTRAINT `fk_user_role` FOREIGN KEY (`roleId`) REFERENCES `role` (`id`),
  CONSTRAINT `fk_user_user_createdBy` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_user_user_parentId` FOREIGN KEY (`parentId`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_user_user_updatedBy` FOREIGN KEY (`updatedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'student','$2a$10$aIE/S3S7dkqn5f7MayN5BekXU.20IS1T9082MlcswgMKyIJ69Tzwe',1,NULL,'Test Student','test.student@gmail.com',NULL,NULL,NULL,NULL,'CREATED',NULL,NULL,NULL,NULL),(2,'parent','$2a$10$aIE/S3S7dkqn5f7MayN5BekXU.20IS1T9082MlcswgMKyIJ69Tzwe',2,NULL,'Test Parent','test.parent@gmail.com',NULL,NULL,NULL,NULL,'CREATED',NULL,NULL,NULL,NULL),(3,'teacher','$2a$10$aIE/S3S7dkqn5f7MayN5BekXU.20IS1T9082MlcswgMKyIJ69Tzwe',3,NULL,'Test Teacher','test.teacher@gmail.com',NULL,NULL,NULL,NULL,'CREATED',NULL,NULL,NULL,NULL),(4,'admin','$2a$10$aIE/S3S7dkqn5f7MayN5BekXU.20IS1T9082MlcswgMKyIJ69Tzwe',4,NULL,'Test Admin','test.admin@gmail.com',NULL,NULL,NULL,NULL,'CREATED',NULL,NULL,NULL,NULL),(5,'staff','$2a$10$aIE/S3S7dkqn5f7MayN5BekXU.20IS1T9082MlcswgMKyIJ69Tzwe',5,NULL,'Test Staff','test.staff@gmail.com',NULL,NULL,NULL,NULL,'CREATED',NULL,NULL,NULL,NULL),(6,'teacher2','$2a$10$aIE/S3S7dkqn5f7MayN5BekXU.20IS1T9082MlcswgMKyIJ69Tzwe',3,NULL,'Test Teacher2','test.teacher2@gmail.com',NULL,NULL,NULL,NULL,'CREATED',NULL,NULL,NULL,NULL),(7,'duong','$2a$10$aIE/S3S7dkqn5f7MayN5BekXU.20IS1T9082MlcswgMKyIJ69Tzwe',1,NULL,'duong','gicungduoc1999@gmail.com',NULL,NULL,NULL,NULL,'CREATED','2023-07-17 19:15:50',NULL,'2023-07-20 18:45:16',NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wallet`
--

DROP TABLE IF EXISTS `wallet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wallet` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `userId` bigint NOT NULL,
  `paymentId` bigint DEFAULT NULL,
  `currentBalance` double NOT NULL,
  `note` varchar(255) DEFAULT NULL,
  `amountChange` double NOT NULL,
  `changeAt` datetime NOT NULL,
  `status` varchar(45) NOT NULL,
  `createdAt` datetime DEFAULT NULL,
  `createdBy` bigint DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  `updatedBy` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_wallet_payment_idx` (`paymentId`),
  KEY `fk_wallet_user_idx` (`userId`),
  KEY `fk_wallet_user_createdBy_idx` (`createdBy`),
  KEY `fk_wallet_user_updatedBy_idx` (`updatedBy`),
  CONSTRAINT `fk_wallet_payment` FOREIGN KEY (`paymentId`) REFERENCES `payment` (`id`),
  CONSTRAINT `fk_wallet_user` FOREIGN KEY (`userId`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_wallet_user_createdBy` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_wallet_user_updatedBy` FOREIGN KEY (`updatedBy`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wallet`
--

LOCK TABLES `wallet` WRITE;
/*!40000 ALTER TABLE `wallet` DISABLE KEYS */;
/*!40000 ALTER TABLE `wallet` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-07-26 21:59:46
