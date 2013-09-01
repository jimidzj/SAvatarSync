/*
SQLyog Ultimate v8.32 
MySQL - 5.5.8 : Database - hrm
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`hrm` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `hrm`;

/*Table structure for table `mt_book` */

DROP TABLE IF EXISTS `mt_book`;

CREATE TABLE `mt_book` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) DEFAULT NULL,
  `roomid` int(11) DEFAULT NULL,
  `startdate` datetime DEFAULT NULL,
  `enddate` datetime DEFAULT NULL,
  `uid` int(11) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;

/*Data for the table `mt_book` */

LOCK TABLES `mt_book` WRITE;

insert  into `mt_book`(`id`,`title`,`roomid`,`startdate`,`enddate`,`uid`,`createtime`) values (1,'测试',1,'2012-01-31 15:00:00','2012-01-31 18:00:00',1,'2012-01-30 00:00:00'),(6,'会议',1,'2012-02-01 05:00:00','2012-02-01 07:00:00',1,'2012-01-31 06:01:02'),(7,'iii8888',1,'2012-01-31 00:15:00','2012-01-31 02:00:00',1,'2012-01-31 06:03:04'),(9,'pppp',2,'2012-02-01 11:00:00','2012-02-01 12:00:00',1,'2012-01-31 06:25:16'),(11,'rrr',1,'2012-01-29 00:30:00','2012-01-29 01:29:00',1,'2012-01-31 07:12:35'),(12,'rttet',1,'2012-02-01 00:00:00','2012-02-01 04:30:00',1,'2012-01-31 07:12:44'),(18,'rrrr',1,'2012-01-31 13:00:00','2012-01-31 13:30:00',1,'2012-02-01 01:42:27'),(19,'777',1,'2012-01-30 00:00:00','2012-01-30 02:00:00',1,'2012-02-01 03:01:40'),(20,'rrr',1,'2012-01-30 03:30:00','2012-01-30 04:30:00',1,'2012-02-01 03:05:13'),(21,'eeee',1,'2012-01-31 06:30:00','2012-01-31 07:30:00',1,'2012-02-01 03:19:07'),(22,'ooo',1,'2012-02-01 08:00:00','2012-02-01 10:00:00',1,'2012-02-01 03:58:12'),(23,'',1,'2012-01-31 09:30:00','2012-01-31 11:00:00',1,'2012-02-01 03:59:50'),(24,'',1,'2012-02-01 11:00:00','2012-02-01 12:00:00',1,'2012-02-01 04:00:23'),(25,'uuu',1,'2012-01-30 07:30:00','2012-01-30 09:00:00',1,'2012-02-01 04:07:32'),(26,'jjj',1,'2012-01-31 11:30:00','2012-01-31 12:00:00',1,'2012-02-01 04:08:15'),(29,'kk',1,'2012-01-30 10:00:00','2012-01-30 13:00:00',1,'2012-02-01 04:19:34'),(30,';;;',1,'2012-01-30 16:00:00','2012-01-30 17:00:00',1,'2012-02-01 05:07:32'),(31,'kk',1,'2012-02-02 00:00:00','2012-02-03 00:00:00',1,'2012-02-01 07:02:59');

UNLOCK TABLES;

/*Table structure for table `mt_room` */

DROP TABLE IF EXISTS `mt_room`;

CREATE TABLE `mt_room` (
  `roomid` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `desc` varchar(1000) DEFAULT NULL,
  `tel` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`roomid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Data for the table `mt_room` */

LOCK TABLES `mt_room` WRITE;

insert  into `mt_room`(`roomid`,`name`,`desc`,`tel`) values (1,'HuangShan',NULL,NULL),(2,'WuYi',NULL,NULL),(3,'YanDang',NULL,NULL);

UNLOCK TABLES;

/*Table structure for table `sec_menu` */

DROP TABLE IF EXISTS `sec_menu`;

CREATE TABLE `sec_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `menuname` varchar(100) DEFAULT NULL,
  `icon` varchar(100) DEFAULT NULL,
  `seqno` int(11) DEFAULT NULL,
  `pid` int(11) DEFAULT NULL,
  `resource` varchar(100) DEFAULT NULL,
  `url` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

/*Data for the table `sec_menu` */

LOCK TABLES `sec_menu` WRITE;

insert  into `sec_menu`(`id`,`menuname`,`icon`,`seqno`,`pid`,`resource`,`url`) values (1,'Admin','/images/left/icon_1.gif',1,0,'main:index',NULL),(2,'PIM','/images/left/icon_2.gif',2,NULL,NULL,'General.htm'),(3,'Leave','/images/left/icon_3.gif',3,NULL,NULL,'General.htm'),(4,'Overtime','/images/left/icon_4.gif',4,NULL,NULL,'General.htm'),(5,'Meeting Room Book','/images/left/icon_5.gif',5,NULL,NULL,'/meeting/index?roomid=1'),(6,'Finance','/images/left/icon_6.gif',6,NULL,NULL,'General.htm'),(7,'Benefits','/images/left/icon_7.gif',7,NULL,NULL,'General.htm'),(8,'Recruitment','/images/left/icon_8.gif',8,NULL,NULL,'General.htm'),(9,'Reports','/images/left/icon_9.gif',9,NULL,NULL,'General.htm'),(10,'Bug Tracker','/images/left/icon_10.gif',10,NULL,NULL,'General.htm'),(11,'Help','/images/left/icon_11.gif',11,NULL,NULL,'General.htm'),(12,'Company Info',NULL,1,1,NULL,NULL),(13,'Job',NULL,2,1,NULL,'General.htm'),(14,'Qualification',NULL,3,1,NULL,'General.htm'),(15,'Race',NULL,4,1,NULL,'General.htm'),(16,'Users',NULL,5,1,NULL,'General.htm'),(17,'Leave',NULL,6,1,NULL,'General.htm'),(18,'Email Notifications',NULL,7,1,NULL,'General.htm'),(19,'Project Info',NULL,8,1,NULL,'General.htm'),(20,'Inport Data',NULL,9,1,NULL,'General.htm'),(21,'Custom Fields',NULL,10,1,NULL,'General.htm'),(22,'Application Assessment',NULL,11,1,NULL,'General.htm'),(23,'General',NULL,1,12,NULL,'General.htm'),(24,'Locations',NULL,2,12,NULL,'General.htm'),(25,'Company Structure',NULL,3,12,NULL,'General.htm'),(26,'Company Property',NULL,4,12,NULL,'General.htm');

UNLOCK TABLES;

/*Table structure for table `sec_permission` */

DROP TABLE IF EXISTS `sec_permission`;

CREATE TABLE `sec_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `resourceid` int(11) NOT NULL,
  `rid` int(11) NOT NULL,
  `permission` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Data for the table `sec_permission` */

LOCK TABLES `sec_permission` WRITE;

insert  into `sec_permission`(`id`,`resourceid`,`rid`,`permission`) values (1,1,1,1),(2,2,1,1),(3,3,1,0);

UNLOCK TABLES;

/*Table structure for table `sec_resource` */

DROP TABLE IF EXISTS `sec_resource`;

CREATE TABLE `sec_resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `resname` varchar(100) DEFAULT NULL,
  `resdesc` text,
  `pid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Data for the table `sec_resource` */

LOCK TABLES `sec_resource` WRITE;

insert  into `sec_resource`(`id`,`resname`,`resdesc`,`pid`) values (1,'main',NULL,0),(2,'dashboard',NULL,1),(3,'meeting',NULL,1);

UNLOCK TABLES;

/*Table structure for table `sec_role` */

DROP TABLE IF EXISTS `sec_role`;

CREATE TABLE `sec_role` (
  `rid` int(11) NOT NULL AUTO_INCREMENT,
  `rolename` varchar(100) DEFAULT NULL,
  `roledesc` text,
  `pid` int(11) DEFAULT NULL,
  PRIMARY KEY (`rid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `sec_role` */

LOCK TABLES `sec_role` WRITE;

insert  into `sec_role`(`rid`,`rolename`,`roledesc`,`pid`) values (1,'Admin','Admin',NULL);

UNLOCK TABLES;

/*Table structure for table `sec_user` */

DROP TABLE IF EXISTS `sec_user`;

CREATE TABLE `sec_user` (
  `uid` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `sec_user` */

LOCK TABLES `sec_user` WRITE;

insert  into `sec_user`(`uid`,`username`,`password`,`status`) values (1,'jame','698d51a19d8a121ce581499d7b701668',1);

UNLOCK TABLES;

/*Table structure for table `sec_userrole` */

DROP TABLE IF EXISTS `sec_userrole`;

CREATE TABLE `sec_userrole` (
  `uid` int(11) NOT NULL,
  `rid` int(11) NOT NULL,
  PRIMARY KEY (`uid`,`rid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sec_userrole` */

LOCK TABLES `sec_userrole` WRITE;

insert  into `sec_userrole`(`uid`,`rid`) values (1,1);

UNLOCK TABLES;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
