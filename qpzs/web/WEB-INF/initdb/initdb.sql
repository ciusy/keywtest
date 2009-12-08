CREATE TABLE `ruojun`.`user` (
  `USER_EMAIL` VARCHAR(64) NOT NULL COMMENT ' email-address and logon name of user',
  `PASSWORD` VARCHAR(45) NOT NULL,
  `NAME` VARCHAR(45) COMMENT 'ture name',
  `NICKNAME` VARCHAR(45) NOT NULL COMMENT 'showed on web pages',
  `SEX` INTEGER UNSIGNED NOT NULL COMMENT '0secret,1male,2female',
  `BIRTH` DATETIME,
  PRIMARY KEY (`USER_EMAIL`),
  INDEX `id_nickname`(`NICKNAME`)
)
ENGINE = InnoDB;
