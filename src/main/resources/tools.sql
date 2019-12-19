SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS  `tbl_batch_baidu_top`;
CREATE TABLE `tbl_batch_baidu_top` (
  `id` varchar(40) NOT NULL,
  `keyword` varchar(1000) DEFAULT NULL,
  `rank` varchar(255) DEFAULT NULL,
  `ind` varchar(255) DEFAULT NULL,
  `batchid` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_batchid` (`batchid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS  `tbl_batch_googlenews_tw`;
CREATE TABLE `tbl_batch_googlenews_tw` (
  `id` varchar(255) NOT NULL,
  `batchid` varchar(255) DEFAULT NULL,
  `datestrutc` varchar(255) DEFAULT NULL,
  `datetime` datetime DEFAULT NULL,
  `keyword` varchar(255) DEFAULT NULL,
  `publish` varchar(255) DEFAULT NULL,
  `rank` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_batchid` (`batchid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS  `tbl_batch_googlenews_us`;
CREATE TABLE `tbl_batch_googlenews_us` (
  `id` varchar(255) NOT NULL,
  `batchid` varchar(255) DEFAULT NULL,
  `datestrutc` varchar(255) DEFAULT NULL,
  `datetime` datetime DEFAULT NULL,
  `keyword` varchar(255) DEFAULT NULL,
  `publish` varchar(255) DEFAULT NULL,
  `rank` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_batchid` (`batchid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS  `tbl_batch_weibo_top`;
CREATE TABLE `tbl_batch_weibo_top` (
  `id` varchar(40) NOT NULL,
  `keyword` varchar(1000) DEFAULT NULL,
  `rank` varchar(255) DEFAULT NULL,
  `ind` varchar(255) DEFAULT NULL,
  `batchid` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_batchid` (`batchid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS  `tbl_schedule`;
CREATE TABLE `tbl_schedule` (
  `batchid` varchar(40) NOT NULL,
  `batchdate` char(8) DEFAULT NULL,
  `batchcreatetime` datetime DEFAULT NULL,
  `batchtype` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`batchid`),
  KEY `index_batchtype` (`batchtype`) USING BTREE,
  KEY `index_batchdate` (`batchdate`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;

