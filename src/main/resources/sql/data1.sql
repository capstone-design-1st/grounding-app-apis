-- properties 테이블에 샘플 데이터 삽입
INSERT INTO properties (property_id, property_name, oneline, view_count, like_count, total_volume, created_at, updated_at, type)
VALUES
    (UNHEX(REPLACE('1111c0f7-0c97-4bd7-a200-0de1392f1df0', '-', '')), '샘플 빌딩', '이 빌딩은 샘플 빌딩입니다.', 0, 0, 0, NOW(), NOW(), 'building')
ON DUPLICATE KEY UPDATE property_id = property_id;

-- fundraises 테이블에 샘플 데이터 삽입, property_id는 방금 삽입된 properties 테이블 데이터 참조
INSERT INTO fundraises (fundraise_id, property_id, progress_rate, progress_amount, investor_count, security_type, issuer, security_count, issue_price, total_fund, subscription_start_date, subscription_end_date, operator_name, operator_introduction)
VALUES
    (UNHEX(REPLACE('2111c0f7-0c97-4bd7-a200-0de1392f1df0', '-', '')), UNHEX(REPLACE('1111c0f7-0c97-4bd7-a200-0de1392f1df0', '-', '')), 0.0, 0, 0, '땡땡증권', 'ABC 회사', 1000, 1000000, 1000000000, '2024-05-19', '2024-12-31', 'XYZ 운영사', '이 운영사는 XYZ입니다.')
ON DUPLICATE KEY UPDATE fundraise_id = fundraise_id;

-- buildings 테이블에 샘플 데이터 삽입, property_id는 방금 삽입된 properties 테이블 데이터 참조
INSERT INTO buildings (building_id, property_id, use_area, main_use, total_floor_area, land_area, scale, completion_date, official_land_price, leaser, lease_start_date, lease_end_date, floor_count)
VALUES
    (UNHEX(REPLACE('3111c0f7-0c97-4bd7-a200-0de1392f1df0', '-', '')), UNHEX(REPLACE('1111c0f7-0c97-4bd7-a200-0de1392f1df0', '-', '')), '1000m²', '사무실', 1000.0, 1000.0, '대형', '2024-01-01', '1000000000', 'ABC 임차인', '2022-01-01', '2025-12-31', '지상20층 / 지하3층')
ON DUPLICATE KEY UPDATE building_id = building_id;

-- locations 테이블에 샘플 데이터 삽입, property_id는 방금 삽입된 properties 테이블 데이터 참조
INSERT INTO locations (location_id, property_id, city, gu, dong, detail)
VALUES
    (UNHEX(REPLACE('4111c0f7-0c97-4bd7-a200-0de1392f1df0', '-', '')), UNHEX(REPLACE('1111c0f7-0c97-4bd7-a200-0de1392f1df0', '-', '')), '서울특별시', '강남구', '역삼동', '테헤란로 212')
ON DUPLICATE KEY UPDATE location_id = location_id;

-- documents 테이블에 샘플 데이터 삽입, property_id는 방금 삽입된 properties 테이블 데이터 참조
INSERT INTO documents (document_id, property_id, cloudfront_url, s3url, title)
VALUES
    (UNHEX(REPLACE('5111c0f7-0c97-4bd7-a200-0de1392f1df0', '-', '')), UNHEX(REPLACE('1111c0f7-0c97-4bd7-a200-0de1392f1df0', '-', '')), 'url', 'url', 'test')
ON DUPLICATE KEY UPDATE document_id = document_id;

-- users 테이블에 샘플 데이터 삽입
INSERT INTO users (user_id, email, password, phone_number, nickname, role, created_at, updated_at, wallet_address)
VALUES
    (UNHEX(REPLACE('2222c0f7-0c97-4bd7-a200-0de1392f1df0', '-', '')), 'test@user.com', '$2a$04$PqAI9RkcXM3QK6A/GkpbCetMX5Bh7Mt9eV5vO/3ULVPPJwG7Vishi', '01012341234', 'test_user', 'USER', NOW(), NOW(), 'rgYAYxsrXonwYs52dDZVBmZCqutPCFu79')
ON DUPLICATE KEY UPDATE user_id = user_id;

-- accounts 테이블에 샘플 데이터 삽입, user_id는 방금 삽입된 users 테이블 데이터 참조
INSERT INTO accounts (account_id, user_id, deposit, total_earning_rate)
VALUES
    (UNHEX(REPLACE('6111c0f7-0c97-4bd7-a200-0de1392f1df0', '-', '')), UNHEX(REPLACE('2222c0f7-0c97-4bd7-a200-0de1392f1df0', '-', '')), 9999999999999999, 0.0)
ON DUPLICATE KEY UPDATE account_id = account_id;


-- user2
INSERT INTO users (user_id, email, password, phone_number, nickname, role, created_at, updated_at, wallet_address)
VALUES
    (UNHEX(REPLACE('2222c0f7-0c97-4bd7-a200-0de1392f1df1', '-', '')), 'test2@user.com', '$2a$04$PqAI9RkcXM3QK6A/GkpbCetMX5Bh7Mt9eV5vO/3ULVPPJwG7Vishi', '01012341234', 'test_user2', 'USER', NOW(), NOW(), 'rnZZwFgkJCaDS2k5EjsowLcxaF6vdsozDa')
ON DUPLICATE KEY UPDATE user_id = user_id;

INSERT INTO accounts (account_id, user_id, deposit, total_earning_rate)
VALUES
    (UNHEX(REPLACE('6111c0f7-0c97-4bd7-a200-0de1392f1df1', '-', '')), UNHEX(REPLACE('2222c0f7-0c97-4bd7-a200-0de1392f1df1', '-', '')), 9999999999999999, 0.0)
ON DUPLICATE KEY UPDATE account_id = account_id;

-- user3

INSERT INTO users (user_id, email, password, phone_number, nickname, role, created_at, updated_at, wallet_address)
VALUES
    (UNHEX(REPLACE('2222c0f7-0c97-4bd7-a200-0de1392f1df2', '-', '')),'test3@user.com' , '$2a$04$PqAI9RkcXM3QK6A/GkpbCetMX5Bh7Mt9eV5vO/3ULVPPJwG7Vishi', '01012341234', 'test_user3', 'USER', NOW(), NOW(), 'r3roFB8dSpw6wPr4QH9JUKnXTaD628Zx9L')
ON DUPLICATE KEY UPDATE user_id = user_id;

INSERT INTO accounts (account_id, user_id, deposit, total_earning_rate)
VALUES
    (UNHEX(REPLACE('6111c0f7-0c97-4bd7-a200-0de1392f1df2', '-', '')), UNHEX(REPLACE('2222c0f7-0c97-4bd7-a200-0de1392f1df2', '-', '')), 9999999999999999, 0.0)
ON DUPLICATE KEY UPDATE account_id = account_id;

