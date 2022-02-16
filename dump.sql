-- MySQL dump 10.19  Distrib 10.3.31-MariaDB, for debian-linux-gnueabihf (armv8l)
--
-- Host: localhost    Database: SocialNetwork
-- ------------------------------------------------------
-- Server version	10.3.31-MariaDB-0+deb10u1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `member`
--

DROP TABLE IF EXISTS `member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `member` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `company` varchar(45) DEFAULT NULL,
  `logo` varchar(2000) DEFAULT NULL,
  `email` varchar(2000) DEFAULT NULL,
  `manager` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `member_users_id` (`manager`),
  CONSTRAINT `fk_member_users` FOREIGN KEY (`manager`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member`
--

LOCK TABLES `member` WRITE;
/*!40000 ALTER TABLE `member` DISABLE KEYS */;
INSERT INTO `member` VALUES (1,'Logitech','https://viktorov.ml/res/commercial/company/Logitech/logo/logitech_logo.png','mail@logitech.com',2),(2,'Meta','https://viktorov.ml/res/commercial/company/Meta/logo/meta_logo.png','mail@facebook.com',3),(3,'Google','https://viktorov.ml/res/commercial/company/Google/logo/logo_one.png','mail@google.com',5),(4,'VK','https://viktorov.ml/res/commercial/company/vk/logo/vk_logo.png','mail@vk.com',6),(5,'Microsoft','https://viktorov.ml/res/commercial/company/Microsoft/logo/Microsoft_logo.png','mail@microsoft.com',4),(6,'Kaspersky','https://viktorov.ml/res/commercial/company/Kaspersky/logo/kaspersky.png','mail@kaspersky.com',7);
/*!40000 ALTER TABLE `member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post`
--

DROP TABLE IF EXISTS `post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `post` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `author` int(11) NOT NULL,
  `type` int(11) NOT NULL DEFAULT 1,
  `source` int(11) DEFAULT NULL,
  `content` varchar(256) NOT NULL,
  `created` int(12) DEFAULT NULL,
  `liked` tinyint(4) DEFAULT 1,
  `likeCount` int(10) DEFAULT NULL,
  `sharedCount` int(10) DEFAULT NULL,
  `commentCount` int(10) DEFAULT NULL,
  `address` varchar(45) DEFAULT NULL,
  `youtube` varchar(15) DEFAULT NULL,
  `location` point DEFAULT NULL,
  `img` varchar(500) DEFAULT NULL,
  `url` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `post_users_id` (`author`),
  KEY `post_postype_id` (`type`),
  KEY `post_source_id` (`source`),
  CONSTRAINT `post_postype` FOREIGN KEY (`type`) REFERENCES `postype` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `post_source` FOREIGN KEY (`source`) REFERENCES `post` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `post_users` FOREIGN KEY (`author`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post`
--

LOCK TABLES `post` WRITE;
/*!40000 ALTER TABLE `post` DISABLE KEYS */;
INSERT INTO `post` VALUES (1,1,1,NULL,'Some very long text for testing application for Android',1642695933,1,5,4,6,NULL,NULL,NULL,NULL,NULL),(2,2,2,1,'Repost to repost',1612299228,1,NULL,3,NULL,NULL,NULL,NULL,NULL,NULL),(3,3,3,NULL,'Сломал все что было, кто ченить будет? \n Смотри геолокацияю ниже',1606785652,0,12,NULL,2,'Kandor',NULL,'\0\0\0\0\0\0\0y@C\�N@\�41�@',NULL,NULL),(4,4,4,NULL,'Popular video on youtube',1605610533,1,111,66,87,NULL,'a4Uzufh4X3A',NULL,NULL,NULL),(5,5,1,NULL,'Warsaw claims that the storming of the border at the guarded border area in Melnik was carried out with the support of the Belarusian special services.',1642698148,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(6,7,3,NULL,'Here lives a super hero',1495562435,1,21,2,3,'Gotham City',NULL,'\0\0\0\0\0\0\0�����7�9�\�\Z��`@',NULL,NULL),(7,8,4,NULL,'Scary armed people',1473405735,1,8,5,7,NULL,'eG0TiCeJ_2w',NULL,NULL,NULL),(8,6,2,7,'Video repost',1498155435,0,1,1,1,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `postype`
--

DROP TABLE IF EXISTS `postype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `postype` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `postype`
--

LOCK TABLES `postype` WRITE;
/*!40000 ALTER TABLE `postype` DISABLE KEYS */;
INSERT INTO `postype` VALUES (1,'POST'),(2,'REPOST'),(3,'EVENTS'),(4,'VIDEO'),(5,'COMMERCIAL');
/*!40000 ALTER TABLE `postype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `promo`
--

DROP TABLE IF EXISTS `promo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `promo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(11) NOT NULL DEFAULT 5,
  `manager` int(11) NOT NULL,
  `content` varchar(500) DEFAULT NULL,
  `img` varchar(2000) DEFAULT NULL,
  `url` varchar(2000) DEFAULT NULL,
  `sharedCount` int(10) DEFAULT NULL,
  `commentCount` int(10) DEFAULT NULL,
  `likeCount` int(10) DEFAULT NULL,
  `weight` int(10) DEFAULT NULL,
  `limitation` int(12) DEFAULT NULL,
  `views` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `promo_member_id` (`manager`),
  KEY `promo_postype_id` (`type`),
  CONSTRAINT `promo_member` FOREIGN KEY (`manager`) REFERENCES `member` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `promo_postype` FOREIGN KEY (`type`) REFERENCES `postype` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `promo`
--

LOCK TABLES `promo` WRITE;
/*!40000 ALTER TABLE `promo` DISABLE KEYS */;
INSERT INTO `promo` VALUES (1,5,1,'Благодаря усилиям конструкторов и разработчиков (и, конечно же, любви к играм в жанре вождения) симуляторы серии Logitech G позволят вам почувствовать себя настоящим гонщиком.','https://viktorov.ml/res/commercial/company/Logitech/promo/logitech_g27_racing_wheel_1.jpg','https://www.logitech.com/',NULL,NULL,NULL,1000,NULL,22),(2,5,2,'The metaverse is the next evolution of social connection.','https://viktorov.ml/res/commercial/company/Meta/promo/promo.jpeg','https://www.facebook.com/Meta/',NULL,NULL,NULL,500,NULL,15),(3,5,3,'ntroducing Google Marketing Platform, a unified advertising and analytics platform for smarter marketing and better results.','https://viktorov.ml/res/commercial/company/Google/promo/google-marketing-platform.png','https://www.google.com/',NULL,NULL,NULL,1200,NULL,3),(4,5,4,'ВКонтакте – универсальное средство для общения и поиска друзей и одноклассников, которым ежедневно пользуются десятки миллионов человек.','https://viktorov.ml/res/commercial/company/vk/promo/scale_1200.png','https://vk.com/',NULL,NULL,NULL,200,NULL,4),(5,5,5,'Collaborate for free with online versions of Microsoft Word, PowerPoint, Excel, and OneNote.','https://viktorov.ml/res/commercial/company/Microsoft/promo/ms_promo.jpg','https://www.microsoft.com/',NULL,NULL,NULL,1100,NULL,10),(6,5,6,'Надежный антивирус от мирового лидера в сфере информационной безопасности.','https://viktorov.ml/res/commercial/company/Kaspersky/promo/antvir.png','https://www.kaspersky.com/',NULL,NULL,NULL,1150,NULL,4);
/*!40000 ALTER TABLE `promo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `email` varchar(45) DEFAULT NULL,
  `avatar` varchar(45) DEFAULT NULL,
  `registry` int(12) DEFAULT NULL,
  `pass` varchar(45) DEFAULT NULL,
  `key` varchar(45) DEFAULT NULL,
  `banned` tinyint(4) DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Admin','mail@gmail.com',NULL,1642684913,NULL,NULL,1),(2,'Andy','mail@mail.ru',NULL,1612299228,NULL,NULL,1),(3,'Filth','mail@yandex.ru',NULL,1605914700,NULL,NULL,1),(4,'RIDan','mail@list.ru',NULL,1606785652,NULL,NULL,1),(5,'Svinsborg','mail@facebook.com',NULL,1605610533,NULL,NULL,1),(6,'Satan','mail@rambler.ru',NULL,1606577272,NULL,NULL,1),(7,'Brute','mail@yahoo.com',NULL,1559061882,NULL,NULL,1),(8,'Lil','mail@vk.com',NULL,1606015712,NULL,NULL,1);
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

-- Dump completed on 2022-02-15 18:26:40
