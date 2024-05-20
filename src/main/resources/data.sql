-- properties 테이블에 샘플 데이터 삽입
INSERT IGNORE INTO properties (property_uuid, property_name, oneline, present_price, view_count, like_count, volume_count, created_at, updated_at)
VALUES
(UNHEX(REPLACE('1111c0f7-0c97-4bd7-a200-0de1392f1df0', '-', '')), '샘플 빌딩', '이 빌딩은 샘플 빌딩입니다.', 1000000000, 0, 0, 0, NOW(), NOW());

-- fundraises 테이블에 샘플 데이터 삽입, property_id는 방금 삽입된 properties 테이블 데이터 참조
INSERT IGNORE INTO fundraises (property_id, progress_rate, deadline, investor_count, security_type, issuer, security_count, issue_price, total_fund, subscription_start_date, subscription_end_date, operator_name, operator_introduction)
VALUES
    (LAST_INSERT_ID(), 0.0, '2025-01-01', 0, '땡땡증권', 'ABC 회사', 1000, 1000000, 1000000000, '2024-05-19', '2024-12-31', 'XYZ 운영사', '이 운영사는 XYZ입니다.');

-- buildings 테이블에 샘플 데이터 삽입, property_id는 방금 삽입된 properties 테이블 데이터 참조
INSERT IGNORE INTO buildings (property_id, use_area, main_use, total_floor_area, land_area, scale, completion_date, official_land_price, leaser, lease_start_date, lease_end_date, floor_count)
VALUES
    (LAST_INSERT_ID(), '1000m²', '사무실', 1000.0, 1000.0, '대형', '2024-01-01', '1000000000', 'ABC 임차인', '2022-01-01', '2025-12-31', '지상20층 / 지하3층');

-- locations 테이블에 샘플 데이터 삽입, property_id는 방금 삽입된 properties 테이블 데이터 참조
INSERT IGNORE INTO locations (property_id, city, gu, dong, detail)
VALUES
    (LAST_INSERT_ID(), '서울특별시', '강남구', '역삼동', '테헤란로 212');

-- users 테이블에 샘플 데이터 삽입
INSERT IGNORE INTO users (user_id, user_uuid, email, password, phone_number, nickname, created_at, updated_at)
VALUES
(1,
    UNHEX(REPLACE('2222c0f7-0c97-4bd7-a200-0de1392f1df0', '-', '')),
    'test@user.com',
    '$2a$16$AQ6glwfKV7yEa1ngeyQXZejBrzaFdkjLo2GFI7mnh2/DdIlRIpJPW', -- Test123456!
    '01012341234',
    'test_user',
    NOW(),
    NOW());

-- accounts 테이블에 샘플 데이터 삽입, user_id는 방금 삽입된 users 테이블 데이터 참조
INSERT IGNORE INTO accounts (user_id, deposit, total_earning_rate)
VALUES
(1,
    9999999999999999,
    0.0);
