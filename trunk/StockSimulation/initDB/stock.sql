
SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for capital_flow
-- ----------------------------
DROP TABLE IF EXISTS `capital_flow`;
CREATE TABLE `capital_flow` (
  `CAPITAL_FLOW_UUID` varchar(32) NOT NULL,
  `USER_UUID` varchar(32) NOT NULL,
  `STO_NAME` varchar(32) DEFAULT NULL,
  `OPERATION_TYPE` varchar(32) DEFAULT NULL,
  `ACTION_TIME` datetime NOT NULL,
  `CHARGE_PRICE` float DEFAULT NULL,
  `CHARGE_AMOUNT` int(11) DEFAULT NULL,
  `FEE` double NOT NULL,
  `LEFT_FEE` double NOT NULL,
  `BROKERAGE` double DEFAULT NULL,
  `STO_CODE` varchar(10) DEFAULT NULL,
  `INVEST_RESULT_UUID` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`CAPITAL_FLOW_UUID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for asset_day_data
-- ----------------------------
DROP TABLE IF EXISTS `asset_day_data`;
CREATE TABLE `asset_day_data` (
  `ASSET_DAY_DATA_UUID` varchar(32) NOT NULL,
  `USER_UUID` varchar(32) NOT NULL,
  `DATE_TIME` datetime DEFAULT NULL,
  `ASSET_VALUE` double NOT NULL,
  `INVEST_RESULT_UUID` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`ASSET_DAY_DATA_UUID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for person
-- ----------------------------
DROP TABLE IF EXISTS `person`;
CREATE TABLE `person` (
  `USER_UUID` varchar(32) NOT NULL,
  `USER_ID` varchar(32) ,
  PRIMARY KEY (`USER_UUID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

