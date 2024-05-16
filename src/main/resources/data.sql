-- properties 테이블에 샘플 데이터 삽입
INSERT IGNORE INTO properties (property_uuid, name, piece_count, piece_price, price, view_count, like_count, created_at, updated_at)
VALUES
    (UNHEX(REPLACE('1111c0f7-0c97-4bd7-a200-0de1392f1df0', '-', '')), '샘플 빌딩', 10, 1000, 10000, 100, 10, NOW(), NOW());

-- buildings 테이블에 샘플 데이터 삽입, property_id는 방금 삽입된 properties 테이블 데이터 참조
INSERT IGNORE INTO buildings (property_id, use_area, main_use, total_floor_area, land_area, scale, completion_date, official_land_price, leaser, lease_start_date, lease_end_date, created_at, updated_at)
VALUES
    (LAST_INSERT_ID(), '오피스', '상업', 2000.00, 500.00, '대형', '2021-01-01', '1000000', 'XYZ 회사', '2022-01-01', '2027-01-01', NOW(), NOW())

-- users 테이블에 샘플 데이터 삽입
INSERT IGNORE INTO USERS (user_id, user_uuid, email, password, phone_number, nickname, created_at, updated_at)
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
INSERT IGNORE INTO accounts (user_id, account_uuid, deposit, total_earining_rate, created_at, updated_at)
VALUES
(1,
    UNHEX(REPLACE('3333c0f7-0c97-4bd7-a200-0de1392f1df0', '-', '')),
    9999999999999999,
    0.0,
    NOW(),
    NOW());

