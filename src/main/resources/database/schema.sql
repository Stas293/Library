CREATE DATABASE IF NOT EXISTS library DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
USE library;

-- -----------------------------------------------------
-- Table `library`.`user_list`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user_list` ;

CREATE TABLE IF NOT EXISTS `user_list` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(255) NOT NULL,
  `first_name` VARCHAR(255) NOT NULL,
  `last_name` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `phone` CHAR(13) NOT NULL DEFAULT '+380003332211',
  `password` VARCHAR(255) NOT NULL,
  `enabled` TINYINT(1) NOT NULL DEFAULT 1,
  `date_created` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `date_updated` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `login_UNIQUE` (`login`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  UNIQUE KEY `phone_UNIQUE` (`phone`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `library`.`role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `library`.`role` ;

CREATE TABLE IF NOT EXISTS `library`.`role` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(255) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_UNIQUE` (`code`),
  UNIQUE KEY `name_UNIQUE` (`name`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `library`.`user_role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `library`.`user_role` ;

CREATE TABLE IF NOT EXISTS `library`.`user_role` (
  `role_id` BIGINT(20) UNSIGNED NOT NULL,
  `user_id` BIGINT(20) UNSIGNED NOT NULL,
  PRIMARY KEY (`role_id`, `user_id`),
  INDEX `fk_role_has_user_list_user_list1_idx` (`user_id`),
  INDEX `fk_role_has_user_list_role_idx` (`role_id`),
  CONSTRAINT `fk_role_has_user_list_role`
    FOREIGN KEY (`role_id`)
    REFERENCES `library`.`role` (`id`),
  CONSTRAINT `fk_role_has_user_list_user_list1`
    FOREIGN KEY (`user_id`)
    REFERENCES `library`.`user_list` (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `library`.`history_orders`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `library`.`history_order` ;

CREATE TABLE IF NOT EXISTS `library`.`history_order` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(20) UNSIGNED NOT NULL,
  `date_created` TIMESTAMP NOT NULL,
  `date_expire` TIMESTAMP NOT NULL,
  `book_name` VARCHAR(255) NOT NULL,
  `status_id` BIGINT(20) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_history_orders_user_list1_idx` (`user_id`),
  CONSTRAINT `fk_history_orders_user_list1`
    FOREIGN KEY (`user_id`)
    REFERENCES `library`.`user_list` (`id`),
    KEY `fk_history_orders_status` (`status_id`),
  CONSTRAINT `fk_history_orders_status` FOREIGN KEY (`status_id`) REFERENCES `status` (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `library`.`books_catalog`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `library`.`books_catalog` ;

CREATE TABLE IF NOT EXISTS `library`.`books_catalog` (
  `book_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `book_name` VARCHAR(255) NOT NULL,
  `book_count` INT UNSIGNED NOT NULL,
  `ISBN` VARCHAR(255) NOT NULL,
  `book_date_publication` TIMESTAMP NOT NULL,
  `fine_per_day` DECIMAL(10, 2) NOT NULL,
  `language` char(5) NOT NULL DEFAULT 'uk_UA',
  PRIMARY KEY (`book_id`),
  unique Key (`ISBN`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `library`.`place_list`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `library`.`place_list` ;

CREATE TABLE IF NOT EXISTS `library`.`place_list` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `library`.`order_list`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `library`.`order_list` ;

CREATE TABLE IF NOT EXISTS `library`.`order_list` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `date_created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `date_expire` TIMESTAMP NULL DEFAULT NULL,
  `book_id` BIGINT(20) UNSIGNED NOT NULL,
  `user_id` BIGINT(20) UNSIGNED NOT NULL,
  `place_id` BIGINT(20) UNSIGNED NOT NULL,
  `status_id` BIGINT(20) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_order_list_books_catalog1_idx` (`book_id`),
  INDEX `fk_order_list_user_list1_idx` (`user_id`),
  INDEX `fk_order_list_place_list1_idx` (`place_id`),
  CONSTRAINT `fk_order_list_books_catalog1`
    FOREIGN KEY (`book_id`)
    REFERENCES `library`.`books_catalog` (`book_id`),
  CONSTRAINT `fk_order_list_user_list1`
    FOREIGN KEY (`user_id`)
    REFERENCES `library`.`user_list` (`id`),
  CONSTRAINT `fk_order_list_place_list1`
    FOREIGN KEY (`place_id`)
    REFERENCES `library`.`place_list` (`id`),
    KEY `fk_order_list_status` (`status_id`),
  CONSTRAINT `fk_order_list_status` FOREIGN KEY (`status_id`) REFERENCES `status` (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `library`.`author_list`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `library`.`author_list` ;

CREATE TABLE IF NOT EXISTS `library`.`author_list` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(255) NOT NULL,
  `last_name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `library`.`author_book`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `library`.`author_book` ;

CREATE TABLE IF NOT EXISTS `library`.`author_book` (
  `author_id` BIGINT(20) UNSIGNED NOT NULL,
  `book_id` BIGINT(20) UNSIGNED NOT NULL,
  PRIMARY KEY (`author_id`, `book_id`),
  INDEX `fk_author_list_has_books_catalog_books_catalog1_idx` (`book_id`),
  INDEX `fk_author_list_has_books_catalog_author_list1_idx` (`author_id`),
  CONSTRAINT `fk_author_list_has_books_catalog_author_list1`
    FOREIGN KEY (`author_id`)
    REFERENCES `library`.`author_list` (`id`),
  CONSTRAINT `fk_author_list_has_books_catalog_books_catalog1`
    FOREIGN KEY (`book_id`)
    REFERENCES `library`.`books_catalog` (`book_id`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `library`.`status`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `library`.`status`;

CREATE TABLE IF NOT EXISTS `library`.`status` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `bclose` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `library`.`next_statuses`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `library`.`next_statuses`;

CREATE TABLE IF NOT EXISTS `library`.`next_statuses` (
  `status` BIGINT(20) UNSIGNED NOT NULL,
  `nextstatus` BIGINT(20) UNSIGNED NOT NULL,
  KEY `fk_next_status_status` (`status`),
  KEY `fk_next_status_status1` (`nextstatus`),
  CONSTRAINT `fk_next_status_status` FOREIGN KEY (`status`) REFERENCES `status` (`id`),
  CONSTRAINT `fk_next_status_status1` FOREIGN KEY (`nextstatus`) REFERENCES `status` (`id`)
) ENGINE=InnoDB;


DELIMITER $$

DROP PROCEDURE IF EXISTS `INSERT_USER_LIST`$$
CREATE PROCEDURE `INSERT_USER_LIST` (IN `u_login` VARCHAR(255) CHARSET utf8, IN `u_first_name` VARCHAR(255) CHARSET utf8, IN `u_last_name` VARCHAR(255) CHARSET utf8, IN `u_email` VARCHAR(255) CHARSET utf8, IN `u_phone` CHAR(13) CHARSET utf8, IN `u_password` VARCHAR(255) CHARSET utf8, IN `u_enabled` BOOLEAN,  OUT `u_id` BIGINT(20) UNSIGNED)  BEGIN
INSERT INTO `user_list` (`login`, `first_name`, `last_name`, `email`, `phone`, `password`, `enabled`) 
VALUES (`u_login`, `u_first_name`, `u_last_name`,`u_email`, `u_phone`, `u_password`, `u_enabled`);

SELECT LAST_INSERT_ID() into `u_id`;

END$$

DROP PROCEDURE IF EXISTS `INSERT_USER_ROLE`$$
CREATE PROCEDURE `INSERT_USER_ROLE` (IN `id_user` VARCHAR(255), IN `id_role` VARCHAR(255) )  BEGIN
INSERT INTO `user_role` (`role_id`, `user_id`)
VALUES (`id_role`, `id_user`);

END$$

DROP PROCEDURE IF EXISTS `GET_USER_PAGE` $$
CREATE PROCEDURE `GET_USER_PAGE` (IN `limit` BIGINT(20), IN `offset` BIGINT(20), IN `search_by` VARCHAR(255), IN `ASC_DESC` VARCHAR(255)) BEGIN
DECLARE stmt varchar(1000);

SET stmt = 'from `user_list` u';
if (`search_by` is not null) and (length(`search_by`) > 0) THEN
	SET stmt = concat(stmt, ' where u.login like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' u.first_name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' u.last_name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' u.email like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' u.phone like ''%', `search_by`, '%''');
end if;
SET stmt = concat(stmt, ' order by login ', `ASC_DESC`);
SET stmt = concat(stmt, ' limit ', `limit`,' offset ', `offset`);

SET @q = concat('select * ',stmt);
PREPARE stm FROM @q;
EXECUTE stm;
DEALLOCATE PREPARE stm;

END$$

DROP PROCEDURE IF EXISTS `GET_AUTHORS_PAGE` $$
CREATE PROCEDURE `GET_AUTHORS_PAGE` (IN `limit` BIGINT(20), IN `offset` BIGINT(20), IN `search_by` VARCHAR(255), IN `ASC_DESC` VARCHAR(255)) BEGIN
DECLARE stmt varchar(1000);

SET stmt = 'from `author_list` u';
if (`search_by` is not null) and (length(`search_by`) > 0) THEN
    SET stmt = concat(stmt, ' where u.first_name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' u.last_name like ''%', `search_by`, '%''');
end if;
SET stmt = concat(stmt, ' order by last_name ', `ASC_DESC`);
SET stmt = concat(stmt, ' limit ', `limit`,' offset ', `offset`);

SET @q = concat('select * ',stmt);
PREPARE stm FROM @q;
EXECUTE stm;
DEALLOCATE PREPARE stm;

END$$


DROP PROCEDURE IF EXISTS `PAGE_GET_BOOKS_BY_AUTHOR_ID` $$
CREATE PROCEDURE `PAGE_GET_BOOKS_BY_AUTHOR_ID` (IN `limit` BIGINT(20), IN `offset` BIGINT(20), IN `author_id` BIGINT(20) UNSIGNED, IN `search_by` VARCHAR(255), IN `ASC_DESC` VARCHAR(255)) BEGIN
DECLARE stmt varchar(1000);

SET stmt = 'from `books_catalog` b';
SET stmt = concat(stmt, ' inner join `author_book` a on b.book_id=a.book_id');
SET stmt = concat(stmt, ' where a.author_id = ', `author_id`);
if (`search_by` is not null) and (length(`search_by`) > 0) THEN
    SET stmt = concat(stmt, ' AND b.book_name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' b.ISBN like ''%', `search_by`, '%''');
end if;
SET stmt = concat(stmt, ' order by book_name ', `ASC_DESC`);
SET stmt = concat(stmt, ' limit ', `limit`,' offset ', `offset`);

SET @q = concat('select * ',stmt);
PREPARE stm FROM @q;
EXECUTE stm;
DEALLOCATE PREPARE stm;

END$$

DROP PROCEDURE IF EXISTS `PAGE_GET_BOOKS_BY_AUTHOR_ID_AND_LANGUAGE` $$
CREATE PROCEDURE `PAGE_GET_BOOKS_BY_AUTHOR_ID_AND_LANGUAGE` (IN `limit` BIGINT(20), IN `offset` BIGINT(20), IN `author_id` BIGINT(20) UNSIGNED, IN `language` VARCHAR(255),  IN `search_by` VARCHAR(255), IN `ASC_DESC` VARCHAR(255)) BEGIN
DECLARE stmt varchar(1000);

SET stmt = 'from `books_catalog` b';
SET stmt = concat(stmt, ' inner join `author_book` a on b.book_id=a.book_id');
SET stmt = concat(stmt, ' where a.author_id = ', `author_id`);
SET stmt = concat(stmt, ' AND b.language = ''', `language`, '''');
if (`search_by` is not null) and (length(`search_by`) > 0) THEN
    SET stmt = concat(stmt, ' AND (b.book_name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' b.ISBN like ''%', `search_by`, '%'' )');
end if;
SET stmt = concat(stmt, ' order by book_name ', `ASC_DESC`);
SET stmt = concat(stmt, ' limit ', `limit`,' offset ', `offset`);

SET @q = concat('select * ',stmt);
PREPARE stm FROM @q;
EXECUTE stm;
DEALLOCATE PREPARE stm;

END$$

DROP PROCEDURE IF EXISTS `PAGE_GET_BOOKS_BY_LANGUAGE` $$
CREATE PROCEDURE `PAGE_GET_BOOKS_BY_LANGUAGE` (IN `limit` BIGINT(20), IN `offset` BIGINT(20), IN `lang` VARCHAR(255),  IN `search_by` VARCHAR(255), IN `ASC_DESC` VARCHAR(255)) BEGIN
DECLARE stmt varchar(1000);

SET stmt = 'from `books_catalog` b';
SET stmt = concat(stmt, ' where b.language = ''', lang, '''');
if (`search_by` is not null) and (length(`search_by`) > 0) THEN
    SET stmt = concat(stmt, ' AND (b.book_name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' b.ISBN like ''%', `search_by`, '%'' )');
end if;
SET stmt = concat(stmt, ' order by book_name ', `ASC_DESC`);
SET stmt = concat(stmt, ' limit ', `limit`,' offset ', `offset`);

SET @q = concat('select * ',stmt);
PREPARE stm FROM @q;
EXECUTE stm;
DEALLOCATE PREPARE stm;

END$$

DROP PROCEDURE IF EXISTS `PAGE_GET_BOOKS` $$
CREATE PROCEDURE `PAGE_GET_BOOKS` (IN `limit` BIGINT(20), IN `offset` BIGINT(20), IN `order_by` VARCHAR(255), IN `search_by` VARCHAR(255), IN `ASC_DESC` VARCHAR(255)) BEGIN
DECLARE stmt varchar(1000);

SET stmt = 'from `books_catalog` b';
SET stmt = concat(stmt, ' inner join `author_book` a on b.book_id=a.book_id');
SET stmt = concat(stmt, ' inner join `author_list` l on a.author_id=l.id');
if (`search_by` is not null) and (length(`search_by`) > 0) THEN
    SET stmt = concat(stmt, ' where b.book_name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' b.ISBN like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' l.first_name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' l.last_name like ''%', `search_by`, '%''');
end if;
SET stmt = concat(stmt, ' group by b.book_id');
if (`ASC_DESC` = 'DESC') THEN
	SET stmt = concat(stmt, ' order by MAX(', `order_by`, ') DESC');
else
    SET stmt = concat(stmt, ' order by MIN(', `order_by`, ') ');
end if;
SET stmt = concat(stmt, ' limit ', `limit`,' offset ', `offset`);

SET @q = concat('select b.*',stmt);
PREPARE stm FROM @q;
EXECUTE stm;
DEALLOCATE PREPARE stm;

END$$

DROP PROCEDURE IF EXISTS `PAGE_GET_BOOKS_NOT_ORDERED` $$
CREATE PROCEDURE `PAGE_GET_BOOKS_NOT_ORDERED` (IN `limit` BIGINT(20), IN `offset` BIGINT(20), IN `order_by` VARCHAR(255), IN `id_user` BIGINT(20), IN `search_by` VARCHAR(255), IN `ASC_DESC` VARCHAR(255)) BEGIN
DECLARE stmt varchar(1000);

SET stmt = 'from `books_catalog` b';
SET stmt = concat(stmt, ' inner join `author_book` a on b.book_id=a.book_id');
SET stmt = concat(stmt, ' inner join `author_list` l on a.author_id=l.id');
SET stmt = concat(stmt, ' WHERE b.book_id NOT IN(
SELECT bc.book_id FROM library.order_list ol inner join library.books_catalog bc on ol.book_id=bc.book_id where ol.user_id = ', `id_user`, ')');
SET stmt = concat(stmt, ' AND b.book_count > 0');
if (`search_by` is not null) and (length(`search_by`) > 0) THEN
    SET stmt = concat(stmt, ' AND (b.book_name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' b.ISBN like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' l.first_name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' l.last_name like ''%', `search_by`, '%'')');
end if;
SET stmt = concat(stmt, ' group by b.book_id');
if (`ASC_DESC` = 'DESC') THEN
	SET stmt = concat(stmt, ' order by MAX(', `order_by`, ') DESC');
else
    SET stmt = concat(stmt, ' order by MIN(', `order_by`, ') ');
end if;
SET stmt = concat(stmt, ' limit ', `limit`,' offset ', `offset`);

SET @q = concat('select b.*',stmt);
PREPARE stm FROM @q;
EXECUTE stm;
DEALLOCATE PREPARE stm;

END$$


DROP PROCEDURE IF EXISTS `GET_PLACES_PAGE` $$
CREATE PROCEDURE `GET_PLACES_PAGE` (IN `limit` BIGINT(20), IN `offset` BIGINT(20), IN `search_by` VARCHAR(255), IN `ASC_DESC` VARCHAR(255)) BEGIN
DECLARE stmt varchar(1000);

SET stmt = 'from `place_list` p';
if (`search_by` is not null) and (length(`search_by`) > 0) THEN
    SET stmt = concat(stmt, ' where p.name like ''%', `search_by`, '%'' or ');
end if;
SET stmt = concat(stmt, ' order by name ', `ASC_DESC`);
SET stmt = concat(stmt, ' limit ', `limit`,' offset ', `offset`);

SET @q = concat('select * ',stmt);
PREPARE stm FROM @q;
EXECUTE stm;
DEALLOCATE PREPARE stm;

END$$


DROP PROCEDURE IF EXISTS `GET_ORDERS_PAGE` $$
CREATE PROCEDURE `GET_ORDERS_PAGE` (IN `limit` BIGINT(20), IN `offset` BIGINT(20), IN `search_by` VARCHAR(255), IN `ASC_DESC` VARCHAR(255)) BEGIN
DECLARE stmt varchar(1000);

SET stmt = 'from `order_list` o';
SET stmt = concat(stmt, ' inner join `user_list` u on o.user_id=u.id');
SET stmt = concat(stmt, ' inner join `books_catalog` b on o.book_id=b.book_id');
if (`search_by` is not null) and (length(`search_by`) > 0) THEN
    SET stmt = concat(stmt, ' where b.book_name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' b.ISBN like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' u.first_name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' u.last_name like ''%', `search_by`, '%''');
end if;
SET stmt = concat(stmt, ' order by o.date_created ', `ASC_DESC`);
SET stmt = concat(stmt, ' limit ', `limit`,' offset ', `offset`);

SET @q = concat('select o.* ', stmt);
PREPARE stm FROM @q;
EXECUTE stm;
DEALLOCATE PREPARE stm;

END$$


DROP PROCEDURE IF EXISTS `GET_ORDERS_PAGE_BY_USER_ID` $$
CREATE PROCEDURE `GET_ORDERS_PAGE_BY_USER_ID` (IN `limit` BIGINT(20), IN `offset` BIGINT(20), IN `id_user` BIGINT(20), IN `search_by` VARCHAR(255), IN `ASC_DESC` VARCHAR(255)) BEGIN
DECLARE stmt varchar(1000);

SET stmt = 'from `order_list` o';
SET stmt = concat(stmt, ' inner join `user_list` u on o.user_id=u.id');
SET stmt = concat(stmt, ' inner join `books_catalog` b on o.book_id=b.book_id');
SET stmt = concat(stmt, ' inner join `place_list` p on o.place_id=p.id');
SET stmt = concat(stmt, ' where o.user_id=', `id_user`);
if (`search_by` is not null) and (length(`search_by`) > 0) THEN
    SET stmt = concat(stmt, ' and (b.book_name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' b.ISBN like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' p.name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' u.first_name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' u.last_name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' o.date_created like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' o.date_expire like ''%', `search_by`, '%'' )');
end if;

SET stmt = concat(stmt, ' order by o.date_created ', `ASC_DESC`);
SET stmt = concat(stmt, ' limit ', `limit`,' offset ', `offset`);

SET @q = concat('select o.* ', stmt);
PREPARE stm FROM @q;
EXECUTE stm;
DEALLOCATE PREPARE stm;

END$$


DROP PROCEDURE IF EXISTS `GET_ORDERS_PAGE_BY_PLACE_ID` $$
CREATE PROCEDURE `GET_ORDERS_PAGE_BY_PLACE_ID` (IN `limit` BIGINT(20), IN `offset` BIGINT(20), IN `id_place` BIGINT(20), IN `search_by` VARCHAR(255), IN `ASC_DESC` VARCHAR(255)) BEGIN
DECLARE stmt varchar(1000);

SET stmt = 'from `order_list` o';
SET stmt = concat(stmt, ' inner join `user_list` u on o.user_id=u.id');
SET stmt = concat(stmt, ' inner join `books_catalog` b on o.book_id=b.book_id');
SET stmt = concat(stmt, ' inner join `place_list` p on o.place_id=p.id');
SET stmt = concat(stmt, ' where o.place_id=', `id_place`);
if (`search_by` is not null) and (length(`search_by`) > 0) THEN
    SET stmt = concat(stmt, ' and (b.book_name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' b.ISBN like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' p.name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' u.first_name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' u.last_name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' o.date_created like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' o.date_expire like ''%', `search_by`, '%'' )');
end if;

SET stmt = concat(stmt, ' order by o.date_created ', `ASC_DESC`);
SET stmt = concat(stmt, ' limit ', `limit`,' offset ', `offset`);

SET @q = concat('select o.* ', stmt);
PREPARE stm FROM @q;
EXECUTE stm;
DEALLOCATE PREPARE stm;

END$$


DROP PROCEDURE IF EXISTS `GET_ORDERS_PAGE_BY_STATUS_ID` $$
CREATE PROCEDURE `GET_ORDERS_PAGE_BY_STATUS_ID` (IN `limit` BIGINT(20), IN `offset` BIGINT(20), IN `id_status` BIGINT(20), IN `search_by` VARCHAR(255), IN `ASC_DESC` VARCHAR(255), IN `SORT_BY` VARCHAR(255)) BEGIN
DECLARE stmt varchar(1000);

SET stmt = 'from `order_list` o';
SET stmt = concat(stmt, ' inner join `user_list` u on o.user_id=u.id');
SET stmt = concat(stmt, ' inner join `books_catalog` b on o.book_id=b.book_id');
SET stmt = concat(stmt, ' inner join `status` p on o.status_id=p.id');
SET stmt = concat(stmt, ' where o.status_id=', `id_status`);
if (`search_by` is not null) and (length(`search_by`) > 0) THEN
    SET stmt = concat(stmt, ' and (b.book_name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' b.ISBN like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' u.first_name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' u.last_name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' o.date_created like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' o.date_expire like ''%', `search_by`, '%'' )');
end if;

SET stmt = concat(stmt, ' order by ', `SORT_BY`, ' ', `ASC_DESC`);
SET stmt = concat(stmt, ' limit ', `limit`,' offset ', `offset`);

SET @q = concat('select o.* ', stmt);
PREPARE stm FROM @q;
EXECUTE stm;
DEALLOCATE PREPARE stm;

END$$


DROP PROCEDURE IF EXISTS `GET_ORDERS_PAGE_BY_USER_ID_AND_PLACE_ID` $$
CREATE PROCEDURE `GET_ORDERS_PAGE_BY_USER_ID_AND_PLACE_ID` (IN `limit` BIGINT(20), IN `offset` BIGINT(20), IN `id_user` BIGINT(20), IN `id_place` BIGINT(20), IN `search_by` VARCHAR(255), IN `ASC_DESC` VARCHAR(255)) BEGIN
DECLARE stmt varchar(1000);

SET stmt = 'from `order_list` o';
SET stmt = concat(stmt, ' inner join `user_list` u on o.user_id=u.id');
SET stmt = concat(stmt, ' inner join `books_catalog` b on o.book_id=b.book_id');
SET stmt = concat(stmt, ' inner join `place_list` p on o.place_id=p.id');
SET stmt = concat(stmt, ' where o.user_id=', `id_user`);
SET stmt = concat(stmt, ' and o.place_id=', `id_place`);
if (`search_by` is not null) and (length(`search_by`) > 0) THEN
    SET stmt = concat(stmt, ' and (b.book_name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' b.ISBN like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' p.name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' u.first_name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' u.last_name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' o.date_created like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' o.date_expire like ''%', `search_by`, '%'' )');
end if;

SET stmt = concat(stmt, ' order by o.date_created ', `ASC_DESC`);
SET stmt = concat(stmt, ' limit ', `limit`,' offset ', `offset`);

SET @q = concat('select o.* ', stmt);
PREPARE stm FROM @q;
EXECUTE stm;
DEALLOCATE PREPARE stm;

END$$

DROP PROCEDURE IF EXISTS `GET_ORDERS_PAGE_BY_STATUS_AND_USER_ID` $$
CREATE PROCEDURE `GET_ORDERS_PAGE_BY_STATUS_AND_USER_ID` (IN `limit` BIGINT(20), IN `offset` BIGINT(20), IN `id_user` BIGINT(20), IN `id_status` BIGINT(20), IN `search_by` VARCHAR(255), IN `ASC_DESC` VARCHAR(255)) BEGIN
DECLARE stmt varchar(1000);

SET stmt = 'from `order_list` o';
SET stmt = concat(stmt, ' inner join `user_list` u on o.user_id=u.id');
SET stmt = concat(stmt, ' inner join `books_catalog` b on o.book_id=b.book_id');
SET stmt = concat(stmt, ' inner join `place_list` p on o.place_id=p.id');
SET stmt = concat(stmt, ' inner join `status` s on o.status_id=s.id');
SET stmt = concat(stmt, ' where o.user_id=', `id_user`);
SET stmt = concat(stmt, ' and o.status_id=', `id_status`);
if (`search_by` is not null) and (length(`search_by`) > 0) THEN
    SET stmt = concat(stmt, ' and (b.book_name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' b.ISBN like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' p.name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' u.first_name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' u.last_name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' o.date_created like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' o.date_expire like ''%', `search_by`, '%'' )');
end if;

SET stmt = concat(stmt, ' order by o.date_created ', `ASC_DESC`);
SET stmt = concat(stmt, ' limit ', `limit`,' offset ', `offset`);

SET @q = concat('select o.* ', stmt);
PREPARE stm FROM @q;
EXECUTE stm;
DEALLOCATE PREPARE stm;

END$$


DROP PROCEDURE IF EXISTS `GET_ORDERS_PAGE_BY_STATUS_AND_PLACE_ID` $$
CREATE PROCEDURE `GET_ORDERS_PAGE_BY_STATUS_AND_PLACE_ID` (IN `limit` BIGINT(20), IN `offset` BIGINT(20), IN `id_place` BIGINT(20), IN `id_status` BIGINT(20), IN `search_by` VARCHAR(255), IN `ASC_DESC` VARCHAR(255), IN `SORT_BY` VARCHAR(255)) BEGIN
DECLARE stmt varchar(1000);

SET stmt = 'from `order_list` o';
SET stmt = concat(stmt, ' inner join `user_list` u on o.user_id=u.id');
SET stmt = concat(stmt, ' inner join `books_catalog` b on o.book_id=b.book_id');
SET stmt = concat(stmt, ' inner join `place_list` p on o.place_id=p.id');
SET stmt = concat(stmt, ' inner join `status` s on o.status_id=s.id');
SET stmt = concat(stmt, ' where o.place_id=', `id_place`);
SET stmt = concat(stmt, ' and o.status_id=', `id_status`);
if (`search_by` is not null) and (length(`search_by`) > 0) THEN
    SET stmt = concat(stmt, ' and (b.book_name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' b.ISBN like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' p.name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' u.first_name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' u.last_name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' o.date_created like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' o.date_expire like ''%', `search_by`, '%'' )');
end if;

SET stmt = concat(stmt, ' order by ', `SORT_BY`, ' ', `ASC_DESC`);
SET stmt = concat(stmt, ' limit ', `limit`,' offset ', `offset`);

SET @q = concat('select o.* ', stmt);
PREPARE stm FROM @q;
EXECUTE stm;
DEALLOCATE PREPARE stm;

END$$

DROP PROCEDURE IF EXISTS `GET_ORDERS_PAGE_BY_PLACE_NAME` $$
CREATE PROCEDURE `GET_ORDERS_PAGE_BY_PLACE_NAME` (IN `limit` BIGINT(20), IN `offset` BIGINT(20), IN `place_name` VARCHAR(255), IN `search_by` VARCHAR(255), IN `ASC_DESC` VARCHAR(255)) BEGIN
DECLARE stmt varchar(1000);

SET stmt = 'from `order_list` o';
SET stmt = concat(stmt, ' inner join `user_list` u on o.user_id=u.id');
SET stmt = concat(stmt, ' inner join `books_catalog` b on o.book_id=b.book_id');
SET stmt = concat(stmt, ' inner join `place_list` p on o.place_id=p.id');
SET stmt = concat(stmt, ' where p.name like ''%', `place_name`, '%''');
if (`search_by` is not null) and (length(`search_by`) > 0) THEN
    SET stmt = concat(stmt, ' and (b.book_name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' b.ISBN like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' u.first_name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' u.last_name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' o.date_created like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' o.date_expire like ''%', `search_by`, '%'' )');
end if;

SET stmt = concat(stmt, ' order by o.date_created ', `ASC_DESC`);
SET stmt = concat(stmt, ' limit ', `limit`,' offset ', `offset`);

SET @q = concat('select o.* ', stmt);
PREPARE stm FROM @q;
EXECUTE stm;
DEALLOCATE PREPARE stm;

END$$


DROP PROCEDURE IF EXISTS `GET_ORDERS_PAGE_BY_BOOK_ID` $$
CREATE PROCEDURE `GET_ORDERS_PAGE_BY_BOOK_ID` (IN `limit` BIGINT(20), IN `offset` BIGINT(20), IN `id_book` BIGINT(20), IN `search_by` VARCHAR(255), IN `ASC_DESC` VARCHAR(255)) BEGIN
DECLARE stmt varchar(1000);

SET stmt = 'from `order_list` o';
SET stmt = concat(stmt, ' inner join `user_list` u on o.user_id=u.id');
SET stmt = concat(stmt, ' inner join `books_catalog` b on o.book_id=b.book_id');
SET stmt = concat(stmt, ' inner join `place_list` p on o.place_id=p.id');
SET stmt = concat(stmt, ' where o.book_id=', `id_book`);
if (`search_by` is not null) and (length(`search_by`) > 0) THEN
    SET stmt = concat(stmt, ' and (b.book_name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' b.ISBN like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' p.name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' u.first_name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' u.last_name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' o.date_created like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' o.date_expire like ''%', `search_by`, '%'' )');
end if;

SET stmt = concat(stmt, ' order by o.date_created ', `ASC_DESC`);
SET stmt = concat(stmt, ' limit ', `limit`,' offset ', `offset`);

SET @q = concat('select o.* ', stmt);
PREPARE stm FROM @q;
EXECUTE stm;
DEALLOCATE PREPARE stm;

END$$


DROP PROCEDURE IF EXISTS `GET_PAGE_HISTORY_ORDER` $$
CREATE PROCEDURE `GET_PAGE_HISTORY_ORDER` (IN `limit` BIGINT(20), IN `offset` BIGINT(20), IN `search_by` VARCHAR(255), IN `ASC_DESC` VARCHAR(255)) BEGIN
DECLARE stmt varchar(1000);

SET stmt = 'from `history_order` o';
SET stmt = concat(stmt, ' inner join `user_list` u on o.user_id=u.id');
if (`search_by` is not null) and (length(`search_by`) > 0) THEN
    SET stmt = concat(stmt, ' where (o.book_name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' u.first_name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' u.last_name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' u.login like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' o.date_created like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' o.date_expire like ''%', `search_by`, '%'' )');
end if;

SET stmt = concat(stmt, ' order by o.date_created ', `ASC_DESC`);
SET stmt = concat(stmt, ' limit ', `limit`,' offset ', `offset`);

SET @q = concat('select o.* ', stmt);
PREPARE stm FROM @q;
EXECUTE stm;
DEALLOCATE PREPARE stm;

END$$


DROP PROCEDURE IF EXISTS `GET_PAGE_HISTORY_ORDER_BY_USER_ID` $$
CREATE PROCEDURE `GET_PAGE_HISTORY_ORDER_BY_USER_ID` (IN `limit` BIGINT(20), IN `offset` BIGINT(20), IN `id_user` BIGINT(20), IN `search_by` VARCHAR(255), IN `ASC_DESC` VARCHAR(255)) BEGIN
DECLARE stmt varchar(1000);

SET stmt = 'from `history_order` o';
SET stmt = concat(stmt, ' inner join `user_list` u on o.user_id=u.id');
SET stmt = concat(stmt, ' where o.user_id=', `id_user`);
if (`search_by` is not null) and (length(`search_by`) > 0) THEN
    SET stmt = concat(stmt, ' and (o.book_name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' u.first_name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' u.last_name like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' u.login like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' o.date_created like ''%', `search_by`, '%'' or ');
    SET stmt = concat(stmt, ' o.date_expire like ''%', `search_by`, '%'' )');
end if;

SET stmt = concat(stmt, ' order by o.date_created ', `ASC_DESC`);
SET stmt = concat(stmt, ' limit ', `limit`,' offset ', `offset`);

SET @q = concat('select o.* ', stmt);
PREPARE stm FROM @q;
EXECUTE stm;
DEALLOCATE PREPARE stm;

END$$



