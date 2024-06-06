-- Modify the column
# ALTER TABLE real_time_transaction_logs
#     MODIFY COLUMN real_time_transaction_log_id BINARY(16) NOT NULL DEFAULT (UNHEX(REPLACE(UUID(), '-', '')));

ALTER TABLE day_transaction_logs
    MODIFY COLUMN day_transaction_log_id BINARY(16) NOT NULL DEFAULT (UNHEX(REPLACE(UUID(), '-', '')));

ALTER TABLE investment_points
    MODIFY COLUMN investment_point_id BINARY(16) NOT NULL DEFAULT (UNHEX(REPLACE(UUID(), '-', '')));

ALTER TABLE news
    MODIFY COLUMN news_id BINARY(16) NOT NULL DEFAULT (UNHEX(REPLACE(UUID(), '-', '')));

ALTER TABLE summaries
    MODIFY COLUMN summary_id BINARY(16) NOT NULL DEFAULT (UNHEX(REPLACE(UUID(), '-', ''))),
    MODIFY created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE quotes
    MODIFY COLUMN quote_id BINARY(16) NOT NULL DEFAULT (UNHEX(REPLACE(UUID(), '-', ''))),
    MODIFY created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE thumbnail_urls
    MODIFY COLUMN thumbnail_url_id BINARY(16) NOT NULL DEFAULT (UNHEX(REPLACE(UUID(), '-', '')));

ALTER TABLE summaries
    MODIFY created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- properties 테이블에 샘플 데이터 삽입
INSERT INTO properties (property_id, property_name, oneline, view_count, like_count, total_volume, created_at, updated_at, type, uploader_wallet_address)
VALUES
    (UNHEX(REPLACE('1111c0f7-0c97-4bd7-a200-0de1392f1df0', '-', '')), '샘플 빌딩', '이 빌딩은 샘플 빌딩입니다.', 0, 0, 0, NOW(), NOW(), 'building', 'rnZZwFgkJCaDS2k5EjsowLcxaF6vdsozDa')
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

-- investment_points 테이블에 샘플 데이터 삽입, property_id는 방금 삽입된 properties 테이블 데이터 참조
-- INSERT INTO investment_points (investment_point_id, property_id, point_name, point_description, point_image_url)
INSERT INTO investment_points (property_id, title)
VALUES
    (UNHEX(REPLACE('1111c0f7-0c97-4bd7-a200-0de1392f1df0', '-', '')), '💰 연 6% 고정 배당금 지급'),
    (UNHEX(REPLACE('1111c0f7-0c97-4bd7-a200-0de1392f1df0', '-', '')), '💰 시세 대비 낮은 공모가, 매각 차익 기대'),
    (UNHEX(REPLACE('1111c0f7-0c97-4bd7-a200-0de1392f1df0', '-', '')), '💰 신도림역 더블 역세권, 오피스 최적 입지')
ON DUPLICATE KEY UPDATE property_id = property_id;

-- news 테이블에 샘플 데이터 삽입, property_id는 방금 삽입된 properties 테이블 데이터 참조
INSERT INTO news (property_id, title, content)
VALUES
    (UNHEX(REPLACE('1111c0f7-0c97-4bd7-a200-0de1392f1df0', '-', '')), '조각투자사 소유, 8호 부동산 \'신도림 핀포인트타워 2호\' 완판', 'content1'),
    (UNHEX(REPLACE('1111c0f7-0c97-4bd7-a200-0de1392f1df0', '-', '')), '루센트블록 소유, 부동산 상품 \'신도림 핀포인트타워 2호\' 공모 시작', 'content2'),
    (UNHEX(REPLACE('1111c0f7-0c97-4bd7-a200-0de1392f1df0', '-', '')), '삼성이 노린다?, 부동산 상품 \'신도림 핀포인트타워 2호\' 공모 시작', 'content3')
ON DUPLICATE KEY UPDATE property_id = property_id;

-- summaries 테이블에 샘플 데이터 삽입, property_id는 방금 삽입된 properties 테이블 데이터 참조
INSERT INTO summaries (property_id, content)
VALUES
    (UNHEX(REPLACE('1111c0f7-0c97-4bd7-a200-0de1392f1df0', '-', '')), '신도림 핀포인트타워 2호에 대한 세 가지 주요 뉴스는 다음과 같습니다. 첫째, 조각투자사가 소유한 이 부동산은 완판되었습니다. 둘째, 루센트블록이 소유한 이 상품의 공모가 시작되었습니다. 셋째, 삼성도 이 부동산 상품의 공모에 관심을 가지고 있습니다. 투자 정보로는 연 6% 고정 배당금이 지급되고, 시세 대비 낮은 공모가로 매각 차익이 기대되며, 신도림역 더블 역세권이라는 최적의 입지를 가지고 있다는 점이 강조됩니다.')
ON DUPLICATE KEY UPDATE property_id = property_id;

-- users 테이블에 샘플 데이터 삽입
INSERT INTO users (user_id, email, password, phone_number, name, role, created_at, updated_at, wallet_address)
VALUES
    (UNHEX(REPLACE('2222c0f7-0c97-4bd7-a200-0de1392f1df0', '-', '')), 'test@user.com', '$2a$04$PqAI9RkcXM3QK6A/GkpbCetMX5Bh7Mt9eV5vO/3ULVPPJwG7Vishi', '01012341234', 'test_user', 'USER', NOW(), NOW(), 'rgYAYxsrXonwYs52dDZVBmZCqutPCFu79')
ON DUPLICATE KEY UPDATE user_id = user_id;

-- accounts 테이블에 샘플 데이터 삽입, user_id는 방금 삽입된 users 테이블 데이터 참조
INSERT INTO accounts (account_id, user_id, deposit, average_earning_rate)
VALUES
    (UNHEX(REPLACE('6111c0f7-0c97-4bd7-a200-0de1392f1df0', '-', '')), UNHEX(REPLACE('2222c0f7-0c97-4bd7-a200-0de1392f1df0', '-', '')), 9999999999999999, 0.0)
ON DUPLICATE KEY UPDATE account_id = account_id;


-- user2
INSERT INTO users (user_id, email, password, phone_number, name, role, created_at, updated_at, wallet_address)
VALUES
    (UNHEX(REPLACE('2222c0f7-0c97-4bd7-a200-0de1392f1df1', '-', '')), 'test2@user.com', '$2a$04$PqAI9RkcXM3QK6A/GkpbCetMX5Bh7Mt9eV5vO/3ULVPPJwG7Vishi', '01012341234', 'test_user2', 'USER', NOW(), NOW(), 'rnZZwFgkJCaDS2k5EjsowLcxaF6vdsozDa')
ON DUPLICATE KEY UPDATE user_id = user_id;

INSERT INTO accounts (account_id, user_id, deposit, average_earning_rate)
VALUES
    (UNHEX(REPLACE('6111c0f7-0c97-4bd7-a200-0de1392f1df1', '-', '')), UNHEX(REPLACE('2222c0f7-0c97-4bd7-a200-0de1392f1df1', '-', '')), 9999999999999999, 0.0)
ON DUPLICATE KEY UPDATE account_id = account_id;


