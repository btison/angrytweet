CREATE TABLE IF NOT EXISTS `ticket` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `area_code` varchar(255) DEFAULT NULL,
  `channel_in` varchar(255) DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `customer` varchar(255) DEFAULT NULL,
  `external_id` varchar(255) DEFAULT NULL,
  `requester` varchar(255) DEFAULT NULL,
  `service` varchar(255) DEFAULT NULL,
  `submitted` datetime DEFAULT NULL,
  `urgent` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ;

CREATE TABLE IF NOT EXISTS `ticket_error` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ticket` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ;
