-- 'properties' 테이블 생성
CREATE TABLE IF NOT EXISTS `properties` (
    `property_id`BINARY(16),
    `price` INT ,
    `piece_count` INT ,
    `piece_price` INT ,
    `name` VARCHAR(50),
    `view_count` BIGINT,
    `like_count` BIGINT,
    `created_at` DATETIME(6) ,
    `updated_at` DATETIME(6) ,
    PRIMARY KEY (`property_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 'thumbnail_urls' 테이블 생성
CREATE TABLE IF NOT EXISTS `thumbnail_urls` (
    `thumbnail_url_id` BINARY(16),
    `property_id` BINARY(16) unique,
    `s3_url` VARCHAR(100) ,
    `cloudfront_url` VARCHAR(100) ,
    PRIMARY KEY (`thumbnail_url_id`),
    FOREIGN KEY (`property_id`) REFERENCES `properties`(`property_id`)
    )ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 'buildings' 테이블 생성
CREATE TABLE IF NOT EXISTS `buildings` (
   `building_id` BINARY(16),
   `property_id` BINARY(16),
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
    )ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 'locations' 테이블 생성
CREATE TABLE IF NOT EXISTS `locations` (
   `location_id` BINARY(16),
   `property_id` BINARY(16),
   `city` VARCHAR(10) ,
    `gu` VARCHAR(10) ,
    `dong` VARCHAR(10) ,
    PRIMARY KEY (`location_id`),
    FOREIGN KEY (`property_id`) REFERENCES `properties`(`property_id`)
    )ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
