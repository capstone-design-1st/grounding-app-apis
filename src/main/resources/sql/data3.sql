-- properties 테이블에 샘플 데이터 삽입
INSERT INTO properties (property_id, property_name, oneline, view_count, like_count, total_volume, created_at, updated_at, type, uploader_wallet_address)
VALUES
    (UNHEX(REPLACE('2222c0f7-0c97-4da7-a200-0de1392f1df1', '-', '')), '샘플 오피스', '이 건물은 샘플 오피스입니다.', 0, 0, 0, NOW(), NOW(), 'office', 'sAbZwFgkJCaDS2k5EjsowLcxaF6vdsozDa')
    ON DUPLICATE KEY UPDATE property_id = property_id;

-- fundraises 테이블에 샘플 데이터 삽입, property_id는 방금 삽입된 properties 테이블 데이터 참조
INSERT INTO fundraises (fundraise_id, property_id, progress_rate, progress_amount, investor_count, security_type, issuer, security_count, issue_price, total_fund, subscription_start_date, subscription_end_date, operator_name, operator_introduction)
VALUES
    (UNHEX(REPLACE('3222c0f7-0c97-4bd7-a200-0de1392f1df1', '-', '')), UNHEX(REPLACE('2222c0f7-0c97-4da7-a200-0de1392f1df1', '-', '')), 0.0, 0, 0, '사이버증권', 'DEF 회사', 2000, 2000000, 2000000000, '2024-06-01', '2024-12-01', 'LMN 운영사', '이 운영사는 LMN입니다.')
    ON DUPLICATE KEY UPDATE fundraise_id = fundraise_id;

-- buildings 테이블에 샘플 데이터 삽입, property_id는 방금 삽입된 properties 테이블 데이터 참조
INSERT INTO buildings (building_id, property_id, use_area, main_use, total_floor_area, land_area, scale, completion_date, official_land_price, leaser, lease_start_date, lease_end_date, floor_count)
VALUES
    (UNHEX(REPLACE('4222c0f7-0c97-4bd7-a200-0de1392f1df1', '-', '')), UNHEX(REPLACE('2222c0f7-0c97-4da7-a200-0de1392f1df1', '-', '')), '1500m²', '상업용', 1500.0, 1500.0, '중형', '2025-02-01', '1500000000', 'DEF 임차인', '2023-02-01', '2026-12-01', '지상25층 / 지하5층')
    ON DUPLICATE KEY UPDATE building_id = building_id;

-- locations 테이블에 샘플 데이터 삽입, property_id는 방금 삽입된 properties 테이블 데이터 참조
INSERT INTO locations (location_id, property_id, city, gu, dong, detail)
VALUES
    (UNHEX(REPLACE('5222c0f7-0c97-4bd7-a200-0de1392f1df1', '-', '')), UNHEX(REPLACE('2222c0f7-0c97-4da7-a200-0de1392f1df1', '-', '')), '서울특별시', '서초구', '서초동', '서초대로 320')
    ON DUPLICATE KEY UPDATE location_id = location_id;

-- documents 테이블에 샘플 데이터 삽입, property_id는 방금 삽입된 properties 테이블 데이터 참조
INSERT INTO documents (document_id, property_id, cloudfront_url, s3url, title)
VALUES
    (UNHEX(REPLACE('6222c0f7-0c97-4bd7-a200-0de1392f1df1', '-', '')), UNHEX(REPLACE('2222c0f7-0c97-4da7-a200-0de1392f1df1', '-', '')), 'cloudfront_url', 's3_url', '샘플 문서')
    ON DUPLICATE KEY UPDATE document_id = document_id;

-- investment_points 테이블에 샘플 데이터 삽입, property_id는 방금 삽입된 properties 테이블 데이터 참조
INSERT INTO investment_points (property_id, title)
VALUES
    (UNHEX(REPLACE('2222c0f7-0c97-4da7-a200-0de1392f1df1', '-', '')), '💰 연 7% 고정 배당금 지급'),
    (UNHEX(REPLACE('2222c0f7-0c97-4da7-a200-0de1392f1df1', '-', '')), '💰 시세 대비 저렴한 공모가, 높은 매각 차익 기대'),
    (UNHEX(REPLACE('2222c0f7-0c97-4da7-a200-0de1392f1df1', '-', '')), '💰 역세권, 상업용 최적 입지')
    ON DUPLICATE KEY UPDATE property_id = property_id;

-- news 테이블에 샘플 데이터 삽입, property_id는 방금 삽입된 properties 테이블 데이터 참조
INSERT INTO news (property_id, title, content)
VALUES
    (UNHEX(REPLACE('2222c0f7-0c97-4da7-a200-0de1392f1df1', '-', '')), '조각투자사 소유, 9호 부동산 \'서초 핀포인트타워 1호\' 완판', 'content1 updated'),
    (UNHEX(REPLACE('2222c0f7-0c97-4da7-a200-0de1392f1df1', '-', '')), '루센트블록 소유, 부동산 상품 \'서초 핀포인트타워 1호\' 공모 시작', 'content2 updated'),
    (UNHEX(REPLACE('2222c0f7-0c97-4da7-a200-0de1392f1df1', '-', '')), '삼성이 노린다?, 부동산 상품 \'서초 핀포인트타워 1호\' 공모 시작', 'content3 updated')
    ON DUPLICATE KEY UPDATE property_id = property_id;

-- summaries 테이블에 샘플 데이터 삽입, property_id는 방금 삽입된 properties 테이블 데이터 참조
INSERT INTO summaries (property_id, content, created_at)
VALUES
    (UNHEX(REPLACE('2222c0f7-0c97-4da7-a200-0de1392f1df1', '-', '')), '서초 핀포인트타워 1호에 대한 세 가지 주요 뉴스는 다음과 같습니다. 첫째, 조각투자사가 소유한 이 부동산은 완판되었습니다. 둘째, 루센트블록이 소유한 이 상품의 공모가 시작되었습니다. 셋째, 삼성도 이 부동산 상품의 공모에 관심을 가지고 있습니다. 투자 정보로는 연 7% 고정 배당금이 지급되고, 시세 대비 저렴한 공모가로 높은 매각 차익이 기대되며, 역세권 상업용 최적 입지를 가지고 있다는 점이 강조됩니다.', NOW())
    ON DUPLICATE KEY UPDATE property_id = property_id;
