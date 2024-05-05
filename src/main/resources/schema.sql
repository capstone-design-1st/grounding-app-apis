-- 'properties' 테이블 생성
CREATE TABLE IF NOT EXISTS `properties` (
    `property_id` BIGINT AUTO_INCREMENT,
    `property_uuid`BINARY(16),
    `price` INT ,
    `piece_count` INT ,
    `piece_price` INT ,
    `name` VARCHAR(50),
    `view_count` BIGINT,
    `like_count` BIGINT,
    `created_at` DATETIME(6) ,
    `updated_at` DATETIME(6) ,
    PRIMARY KEY (`property_id`),
    UNIQUE KEY (`property_uuid`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 'thumbnail_urls' 테이블 생성
CREATE TABLE IF NOT EXISTS `thumbnail_urls` (
    `thumbnail_url_id` BIGINT AUTO_INCREMENT,
    `property_id` BIGINT unique,
    `s3_url` VARCHAR(100) ,
    `cloudfront_url` VARCHAR(100) ,
    PRIMARY KEY (`thumbnail_url_id`),
    FOREIGN KEY (`property_id`) REFERENCES `properties`(`property_id`)
    )ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 'buildings' 테이블 생성
CREATE TABLE IF NOT EXISTS `buildings` (
   `building_id` BIGINT  AUTO_INCREMENT,
   `property_id` BIGINT ,
    `use_area` VARCHAR(20) ,
    `main_use` VARCHAR(20) ,
    `total_floor_area` DOUBLE,
    `land_area` DOUBLE,
    `scale` VARCHAR(20),
    `completion_date` DATE,
    `official_land_price` VARCHAR(20),
    `leaser` VARCHAR(20),
    `lease_start_date` DATE,
    `lease_end_date` DATE,
    PRIMARY KEY (`building_id`),
    FOREIGN KEY (`property_id`) REFERENCES `properties`(`property_id`)
    )ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 'locations' 테이블 생성
CREATE TABLE IF NOT EXISTS `locations` (
   `location_id` BIGINT  AUTO_INCREMENT,
   `property_id` BIGINT ,
   `city` VARCHAR(10) ,
    `gu` VARCHAR(10) ,
    `dong` VARCHAR(10) ,
    PRIMARY KEY (`location_id`),
    FOREIGN KEY (`property_id`) REFERENCES `properties`(`property_id`)
    )ENGINE=InnoDB DEFAULT CHARSET=utf8;
