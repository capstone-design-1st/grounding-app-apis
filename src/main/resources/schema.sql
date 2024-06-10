-- Create tables if not exists
CREATE TABLE IF NOT EXISTS accounts (
                                        account_id BINARY(16) NOT NULL,
                                        average_earning_rate DOUBLE DEFAULT 0.0,
                                        deposit BIGINT DEFAULT 0,
                                        user_id BINARY(16) NOT NULL,
                                        PRIMARY KEY (account_id),
                                        UNIQUE (user_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS buildings (
                                         building_id BINARY(16) NOT NULL,
                                         completion_date DATE,
                                         floor_count VARCHAR(255),
                                         land_area FLOAT(53),
                                         lease_end_date DATE,
                                         lease_start_date DATE,
                                         leaser VARCHAR(20),
                                         main_use VARCHAR(20),
                                         official_land_price VARCHAR(20),
                                         scale VARCHAR(20),
                                         total_floor_area FLOAT(53),
                                         use_area VARCHAR(20),
                                         property_id BINARY(16) NOT NULL,
                                         PRIMARY KEY (building_id),
                                         UNIQUE (property_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS day_transaction_logs (
                                                    day_transaction_log_id BINARY(16) NOT NULL,
                                                    closing_price INTEGER,
                                                    date DATE NOT NULL,
                                                    fluctuation_rate FLOAT(53),
                                                    max_price INTEGER,
                                                    min_price INTEGER,
                                                    opening_price INTEGER,
                                                    volume_count BIGINT,
                                                    property_id BINARY(16) NOT NULL,
                                                    PRIMARY KEY (day_transaction_log_id),
                                                    UNIQUE (date, property_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS deposit_logs (
                                            deposit_log_id BINARY(16) NOT NULL,
                                            amount BIGINT,
                                            created_at DATETIME(6) NOT NULL,
                                            type VARCHAR(10),
                                            account_id BINARY(16) NOT NULL,
                                            PRIMARY KEY (deposit_log_id),
                                            UNIQUE (account_id, created_at)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS disclosures (
                                           disclosure_id BINARY(16) NOT NULL,
                                           content TINYTEXT,
                                           reported_at DATE,
                                           title VARCHAR(255),
                                           property_id BINARY(16) NOT NULL,
                                           PRIMARY KEY (disclosure_id),
                                           UNIQUE (title, property_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS documents (
                                         document_id BINARY(16) NOT NULL,
                                         cloudfront_url VARCHAR(255),
                                         s3_url VARCHAR(255),
                                         title VARCHAR(255),
                                         property_id BINARY(16) NOT NULL,
                                         PRIMARY KEY (document_id),
                                         UNIQUE (cloudfront_url, property_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS fundraises (
                                          fundraise_id BINARY(16) NOT NULL,
                                          investor_count INT DEFAULT 0,
                                          issue_price INT DEFAULT 0,
                                          issuer VARCHAR(20) DEFAULT '주식회사',
                                          operator_introduction TINYTEXT,
                                          operator_name VARCHAR(20),
                                          progress_amount BIGINT DEFAULT 0,
                                          progress_rate DOUBLE DEFAULT 0.0,
                                          security_count INT DEFAULT 0,
                                          security_type VARCHAR(20) DEFAULT '주식',
                                          subscription_end_date DATE,
                                          subscription_start_date DATE,
                                          total_fund BIGINT DEFAULT 0,
                                          property_id BINARY(16) NOT NULL,
                                          PRIMARY KEY (fundraise_id),
                                          UNIQUE (property_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS inventories (
                                          inventory_id BINARY(16) NOT NULL,
                                          average_buying_price INTEGER,
                                          earnings_rate FLOAT(53),
                                          quantity INTEGER,
                                          sellable_quantity INTEGER,
                                          account_id BINARY(16),
                                          property_id BINARY(16),
                                          PRIMARY KEY (inventory_id),
                                          UNIQUE (account_id, property_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS investment_points (
                                                 investment_point_id BINARY(16) NOT NULL,
                                                 title VARCHAR(50),
                                                 property_id BINARY(16) NOT NULL,
                                                 PRIMARY KEY (investment_point_id),
                                                 UNIQUE (investment_point_id, title, property_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS lands (
                                     land_id BINARY(16) NOT NULL,
                                     area VARCHAR(20),
                                     etc VARCHAR(50),
                                     land_use VARCHAR(30),
                                     nearest_station VARCHAR(50),
                                     parking TINYINT,
                                     recommend_use VARCHAR(30),
                                     property_id BINARY(16) NOT NULL,
                                     PRIMARY KEY (land_id),
                                     UNIQUE (property_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS likes (
                                     like_id BINARY(16) NOT NULL,
                                     created_at DATETIME(6) NOT NULL,
                                     property_id BINARY(16) NOT NULL,
                                     user_id BINARY(16) NOT NULL,
                                     PRIMARY KEY (like_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS locations (
                                         location_id BINARY(16) NOT NULL,
                                         city VARCHAR(10),
                                         detail VARCHAR(10),
                                         dong VARCHAR(10),
                                         gu VARCHAR(10),
                                         property_id BINARY(16) NOT NULL,
                                         PRIMARY KEY (location_id),
                                         UNIQUE (property_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS news (
                                    news_id BINARY(16) NOT NULL,
                                    cloudfront_url VARCHAR(200),
                                    content TEXT,
                                    publisher VARCHAR(255),
                                    reported_at DATE,
                                    s3_url VARCHAR(200),
                                    title VARCHAR(50),
                                    url VARCHAR(200),
                                    property_id BINARY(16) NOT NULL,
                                    PRIMARY KEY (news_id),
                                    UNIQUE (title, property_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS orders (
                                      order_id BINARY(16) NOT NULL,
                                      created_at DATETIME(6) NOT NULL,
                                      price INTEGER,
                                      quantity BIGINT,
                                      status VARCHAR(10),
                                      type VARCHAR(10),
                                      property_id BINARY(16) NOT NULL,
                                      user_id BINARY(16) NOT NULL,
                                      PRIMARY KEY (order_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS properties (
                                          property_id BINARY(16) NOT NULL,
                                          created_at DATETIME(6) NOT NULL,
                                          like_count BIGINT,
                                          property_name VARCHAR(50),
                                          oneline VARCHAR(50),
                                          total_volume BIGINT,
                                          type VARCHAR(255) NOT NULL,
                                          updated_at DATETIME(6),
                                          uploader_wallet_address VARCHAR(255),
                                          view_count BIGINT,
                                          PRIMARY KEY (property_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS quotes (
                                      quote_id BINARY(16) NOT NULL,
                                      created_at DATETIME(6) NOT NULL,
                                      price INTEGER,
                                      quantity INTEGER,
                                      type VARCHAR(255) NOT NULL,
                                      order_id BINARY(16),
                                      property_id BINARY(16),
                                      PRIMARY KEY (quote_id),
                                      UNIQUE (property_id, order_id, created_at)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS real_time_transaction_logs (
                                                          real_time_transaction_log_id BINARY(16) NOT NULL,
                                                          executed_at DATETIME(6) NOT NULL,
                                                          executed_price INTEGER,
                                                          fluctuation_rate FLOAT(53),
                                                          quantity INTEGER,
                                                          property_id BINARY(16) NOT NULL,
                                                          PRIMARY KEY (real_time_transaction_log_id),
                                                          UNIQUE (executed_at, property_id, quantity),
                                                          UNIQUE (executed_at)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS representation_photo_urls (
                                                         representation_photo_url_id BINARY(16) NOT NULL,
                                                         cloudfront_url VARCHAR(255) NOT NULL,
                                                         s3url VARCHAR(255),
                                                         property_id BINARY(16) NOT NULL,
                                                         PRIMARY KEY (representation_photo_url_id),
                                                         UNIQUE (cloudfront_url, property_id),
                                                         UNIQUE (cloudfront_url)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS summaries (
                                         summary_id BINARY(16) NOT NULL,
                                         content TEXT,
                                         created_at DATETIME(6) NOT NULL,
                                         property_id BINARY(16) NOT NULL,
                                         PRIMARY KEY (summary_id),
                                         UNIQUE (property_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS thumbnail_urls (
                                              thumbnail_url_id BINARY(16) NOT NULL,
                                              cloudfront_url VARCHAR(100),
                                              s3_url VARCHAR(100),
                                              property_id BINARY(16) NOT NULL,
                                              PRIMARY KEY (thumbnail_url_id),
                                              UNIQUE (property_id, cloudfront_url),
                                              UNIQUE (property_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS users (
                                     user_id BINARY(16) NOT NULL,
                                     created_at DATETIME(6) NOT NULL,
                                     email VARCHAR(30) NOT NULL,
                                     name VARCHAR(10),
                                     password VARCHAR(100),
                                     phone_number VARCHAR(30),
                                     role ENUM('USER'),
                                     updated_at DATETIME(6),
                                     wallet_address VARCHAR(255),
                                     PRIMARY KEY (user_id)
) ENGINE=InnoDB;
