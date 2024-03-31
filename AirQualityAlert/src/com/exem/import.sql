CREATE DATABASE `airquality` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci */;

use airquality;

CREATE TABLE `station` (
  `StationCode` varchar(20) NOT NULL,
  `StationName` varchar(45) NOT NULL,
  PRIMARY KEY (`StationCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

CREATE TABLE `dust_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `StationCode` varchar(20) NOT NULL,
  `check_Day` varchar(10) NOT NULL,
  `check_Hour` int(11) NOT NULL,
  `pm10` int(11) NOT NULL,
  `pm25` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index2` (`StationCode`,`check_Day`,`check_Hour`),
  CONSTRAINT `fk_dust_data` FOREIGN KEY (`StationCode`) REFERENCES `station` (`StationCode`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=18601 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

CREATE TABLE `check_station` (
  `StationCode` varchar(20) NOT NULL,
  `check_startDate` varchar(20) NOT NULL,
  `check_endDate` varchar(20) NOT NULL,
  PRIMARY KEY (`check_startDate`,`StationCode`),
  KEY `index2` (`StationCode`,`check_endDate`),
  CONSTRAINT `StationCode_check_station` FOREIGN KEY (`StationCode`) REFERENCES `station` (`StationCode`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

CREATE TABLE `alert_data` (
  `dust_grade` int(11) NOT NULL,
  `dust_alert` varchar(45) NOT NULL,
  PRIMARY KEY (`dust_grade`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
insert into alert_data values("1","초미세먼지 경보"),("2","미세먼지 경보"),("3","초미세먼지 주의보"),("4","미세먼지 주의보");

CREATE TABLE `dust_alert` (
  `dust_id` int(11) NOT NULL,
  `dust_grade` int(11) NOT NULL,
  `alert_time` varchar(20) NOT NULL,
  PRIMARY KEY (`dust_id`),
  KEY `dust_alert_grade_idx` (`dust_grade`),
  CONSTRAINT `dust_alert_grade` FOREIGN KEY (`dust_grade`) REFERENCES `alert_data` (`dust_grade`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `dust_alert_id` FOREIGN KEY (`dust_id`) REFERENCES `dust_data` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
