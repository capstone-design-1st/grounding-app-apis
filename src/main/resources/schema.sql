-- 'users' 테이블 생성
CREATE TABLE IF NOT EXISTS `users` (
   `user_id` BIGINT NOT NULL AUTO_INCREMENT,
   `user_uuid` BINARY(16) NOT NULL,
    `email` VARCHAR(30) NOT NULL,
    `password` VARCHAR(100) NOT NULL,
    `phone_number` VARCHAR(30) NOT NULL,
    `nickname` VARCHAR(10) NOT NULL,
    `created_at` DATETIME(6) NOT NULL,
    `updated_at` DATETIME(6),
    PRIMARY KEY (`user_id`)
    );

-- 'profile_img_urls' 테이블 생성
CREATE TABLE IF NOT EXISTS `profile_img_urls` (
  `profile_img_url_id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `s3_url` VARCHAR(100) NOT NULL,
    `cloudfront_url` VARCHAR(100) NOT NULL,
    PRIMARY KEY (`profile_img_url_id`),
    FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`)
    );

-- 'properties' 테이블 생성
CREATE TABLE IF NOT EXISTS `properties` (
    `property_id` BIGINT NOT NULL AUTO_INCREMENT,
    `property_uuid` BINARY(16) NOT NULL,
    `name` VARCHAR(10) NOT NULL,
    `password` VARCHAR(100) NOT NULL,
    `phone_number` VARCHAR(20) NOT NULL,
    `nickname` VARCHAR(10) NOT NULL,
    `view_count` BIGINT,
    `like_count` BIGINT,
    `created_at` DATETIME(6) NOT NULL,
    `updated_at` DATETIME(6) NOT NULL,
    PRIMARY KEY (`property_id`)
    );

-- 'thumbnail_urls' 테이블 생성
CREATE TABLE IF NOT EXISTS `thumbnail_urls` (
    `profile_img_url_id` BIGINT NOT NULL,
    `property_id` BIGINT NOT NULL,
    `s3_url` VARCHAR(100) NOT NULL,
    `cloudfront_url` VARCHAR(100) NOT NULL,
    PRIMARY KEY (`profile_img_url_id`, `property_id`),
    FOREIGN KEY (`profile_img_url_id`) REFERENCES `profile_img_urls`(`profile_img_url_id`),
    FOREIGN KEY (`property_id`) REFERENCES `properties`(`property_id`)
    );

-- 'buildings' 테이블 생성
CREATE TABLE IF NOT EXISTS `buildings` (
   `building_id` BIGINT NOT NULL AUTO_INCREMENT,
   `property_id` BIGINT NOT NULL,
   `land_uuid` BINARY(16) NOT NULL,
    `use_area` VARCHAR(20) NOT NULL,
    `main_use` VARCHAR(20) NOT NULL,
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
    );

-- 'locations' 테이블 생성
CREATE TABLE IF NOT EXISTS `locations` (
   `location_id` BIGINT NOT NULL AUTO_INCREMENT,
   `property_id` BIGINT NOT NULL,
   `city` VARCHAR(10) NOT NULL,
    `gu` VARCHAR(10) NOT NULL,
    `dong` VARCHAR(10) NOT NULL,
    PRIMARY KEY (`location_id`),
    FOREIGN KEY (`property_id`) REFERENCES `properties`(`property_id`)
    );

-- 'lands' 테이블 생성
CREATE TABLE IF NOT EXISTS `lands` (
   `building_id` BIGINT NOT NULL,
   `property_id` BIGINT NOT NULL,
   `land_uuid` BINARY(16) NOT NULL,
    `use_area` VARCHAR(20) NOT NULL,
    `main_use` VARCHAR(20) NOT NULL,
    `total_floor_area` DOUBLE,
    `land_area` DOUBLE,
    `scale` VARCHAR(20),
    `completion_date` DATE,
    `official_land_price` VARCHAR(20),
    `leaser` VARCHAR(20),
    `lease_start_date` DATE,
    `lease_end_date` DATE,
    PRIMARY KEY (`building_id`, `property_id`),
    FOREIGN KEY (`building_id`) REFERENCES `buildings`(`building_id`),
    FOREIGN KEY (`property_id`) REFERENCES `properties`(`property_id`)
    );

-- 'likes' 테이블 생성
CREATE TABLE IF NOT EXISTS `likes` (
   `like_id` BIGINT NOT NULL AUTO_INCREMENT,
   `user_id` BIGINT NOT NULL,
   `property_id` BIGINT NOT NULL,
   PRIMARY KEY (`like_id`),
    FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`),
    FOREIGN KEY (`property_id`) REFERENCES `properties`(`property_id`)
    );

-- 'investment_points' 테이블 생성
CREATE TABLE IF NOT EXISTS `investment_points` (
   `investment_point_id` BIGINT NOT NULL AUTO_INCREMENT,
   `s3_url` VARCHAR(100) NOT NULL,
    `cloudfront_url` VARCHAR(100) NOT NULL,
    `title` VARCHAR(50) NOT NULL,
    `content` TEXT NOT NULL,
    `property_id` BIGINT NOT NULL,
    `summary_id` BIGINT NOT NULL,
    PRIMARY KEY (`investment_point_id`),
    FOREIGN KEY (`property_id`) REFERENCES `properties`(`property_id`),
    FOREIGN KEY (`summary_id`) REFERENCES `summaries`(`summary_id`)
    );

-- 'news' 테이블 생성
CREATE TABLE IF NOT EXISTS `news` (
  `news_id` BIGINT NOT NULL AUTO_INCREMENT,
  `s3_url` VARCHAR(100) NOT NULL,
    `cloudfront_url` VARCHAR(100) NOT_NULL,
    `title` VARCHAR(50) NOT NULL,
    `content` TEXT NOT NULL,
    `reported_at` DATE NOT NULL,
    `property_id` BIGINT NOT NULL,
    `summary_id` BIGINT NOT NULL,
    PRIMARY KEY (`news_id`),
    FOREIGN KEY (`property_id`) REFERENCES `properties`(`property_id`),
    FOREIGN KEY (`summary_id`) REFERENCES `summaries`(`summary_id`)
    );

-- 'summaries' 테이블 생성
CREATE TABLE IF NOT EXISTS `summaries` (
    `summary_id` BIGINT NOT NULL AUTO_INCREMENT,
    `content` TEXT NOT NULL,
    `created_at` DATETIME(6) NOT NULL,
    PRIMARY KEY (`summary_id`)
    );

-- 'order' 테이블 생성
CREATE TABLE IF NOT EXISTS `order` (
   `order_id` BIGINT NOT NULL AUTO_INCREMENT,
   `type` VARCHAR(10) NOT NULL,
    `price` INT NOT NULL,
    `amount` INT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `property_id` BIGINT NOT NULL,
    PRIMARY KEY (`order_id`),
    FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`),
    FOREIGN KEY (`property_id`) REFERENCES `properties`(`property_id`)
    );

-- 'real_time_transaction_logs' 테이블 생성
CREATE TABLE IF NOT EXISTS `real_time_transaction_logs` (
    `real_time_transaction_log_id` BIGINT NOT NULL AUTO_INCREMENT,
    `property_id` BIGINT NOT NULL,
    `executed_at` DATETIME(6) NOT NULL,
    `amount` INT NOT NULL,
    `executed_price` INT NOT NULL,
    `fluctuation_rate` DOUBLE NOT NULL,
    `user_id` BIGINT NOT NULL,
    PRIMARY KEY (`real_time_transaction_log_id`),
    FOREIGN KEY (`property_id`) REFERENCES `properties`(`property_id`),
    FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`)
    );

-- 'day_transaction_logs' 테이블 생성
CREATE TABLE IF NOT EXISTS `day_transaction_logs` (
  `day_transaction_log_id` BIGINT NOT NULL AUTO_INCREMENT,
  `property_id` BIGINT NOT NULL,
  `date` DATE NOT NULL,
  `amount` INT NOT NULL,
  `executed_price` INT NOT NULL,
  `fluctuation_rate` DOUBLE NOT NULL,
  PRIMARY KEY (`day_transaction_log_id`),
    FOREIGN KEY (`property_id`) REFERENCES `properties`(`property_id`)
    );

-- 'quotes' 테이블 생성
CREATE TABLE IF NOT EXISTS `quotes` (
    `transaction_id` BIGINT NOT NULL AUTO_INCREMENT,
    `property_id` BIGINT NOT NULL,
    `created_at` DATETIME(6) NOT NULL,
    `day_max_price` INT NOT NULL,
    `day_min_price` INT NOT NULL,
    `present_price` INT NOT NULL,
    PRIMARY KEY (`transaction_id`),
    FOREIGN KEY (`property_id`) REFERENCES `properties`(`property_id`)
    );

-- 'accounts' 테이블 생성
CREATE TABLE IF NOT EXISTS `accounts` (
  `account_id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `account_uuid` BINARY(16) NOT NULL,
    `deposit` BIGINT NOT NULL,
    PRIMARY KEY (`account_id`),
    FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`)
    );

-- 'deposit_logs' 테이블 생성
CREATE TABLE IF NOT EXISTS `deposit_logs` (
  `deposit_log_id` BIGINT NOT NULL AUTO_INCREMENT,
  `account_id` BIGINT NOT NULL,
  `user_id` BIGINT NOT NULL,
  `type` VARCHAR(10) NOT NULL,
    PRIMARY KEY (`deposit_log_id`),
    FOREIGN KEY (`account_id`) REFERENCES `accounts`(`account_id`),
    FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`)
    );
