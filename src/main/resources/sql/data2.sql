-- properties 테이블에 샘플 데이터 삽입
INSERT INTO properties (property_id, property_name, oneline, view_count, like_count, total_volume, created_at, updated_at, type)
VALUES
    (UNHEX(REPLACE('2222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), '예시 임야', '이 건물은 예시 임야입니다.', 0, 0, 20000000, '2023-01-01 00:00:00', NOW(), 'land')
    ON DUPLICATE KEY UPDATE property_id = property_id;

-- fundraises 테이블에 샘플 데이터 삽입, property_id는 방금 삽입된 properties 테이블 데이터 참조
INSERT INTO fundraises (fundraise_id, property_id, progress_rate, progress_amount, investor_count, security_type, issuer, security_count, issue_price, total_fund, subscription_start_date, subscription_end_date, operator_name, operator_introduction)
VALUES
    (UNHEX(REPLACE('3222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), UNHEX(REPLACE('2222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), 100.0, 10000000, 261, '보통증권', 'DEF 회사', 2000, 5000, 10000000, '2023-01-01', '2023-02-01', 'LMN 운영사', '이 운영사는 LMN입니다.')
    ON DUPLICATE KEY UPDATE fundraise_id = fundraise_id;

-- lands 테이블에 샘플 데이터 삽입, property_id는 방금 삽입된 properties 테이블 데이터 참조
INSERT INTO lands (land_id, property_id, area, land_use, etc, recommend_use, parking, nearest_station)
VALUES
    (UNHEX(REPLACE('4222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), UNHEX(REPLACE('2222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), '105,785㎡(32,000평)', '상업용지', '조망 뷰 좋습니다.', '숲경영체험림', 1, '김천역')
    ON DUPLICATE KEY UPDATE land_id = land_id;

-- locations 테이블에 샘플 데이터 삽입, property_id는 방금 삽입된 properties 테이블 데이터 참조
INSERT INTO locations (location_id, property_id, city, gu, dong, detail)
VALUES
    (UNHEX(REPLACE('5222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), UNHEX(REPLACE('2222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), '경상북도', '김천시', '부항면', '안간리')
    ON DUPLICATE KEY UPDATE location_id = location_id;

-- DayTransactionLog 테이블에 1개월치(32일) 테스트 데이터 삽입
INSERT INTO day_transaction_logs (day_transaction_log_id, property_id, date, fluctuation_rate, opening_price, closing_price, max_price, min_price, volume_count)
VALUES
    (UNHEX(REPLACE('6222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), UNHEX(REPLACE('2222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), '2024-05-01', 1.0, 5000, 5050, 5100, 4950, 1500),
    (UNHEX(REPLACE('2422c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), UNHEX(REPLACE('2222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), '2024-05-02', -1.0, 5050, 5000, 5100, 4950, 1600),
    (UNHEX(REPLACE('7222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), UNHEX(REPLACE('2222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), '2024-05-03', 1.5, 5000, 5075, 5150, 4950, 1700),
    (UNHEX(REPLACE('8222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), UNHEX(REPLACE('2222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), '2024-05-04', -0.5, 5075, 5050, 5100, 5000, 1500),
    (UNHEX(REPLACE('9222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), UNHEX(REPLACE('2222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), '2024-05-05', 2.0, 5050, 5150, 5200, 5000, 1800),
    (UNHEX(REPLACE('1322c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), UNHEX(REPLACE('2222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), '2024-05-06', -1.5, 5150, 5075, 5200, 5050, 1700),
    (UNHEX(REPLACE('2452c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), UNHEX(REPLACE('2222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), '2024-05-07', 1.0, 5075, 5125, 5150, 5050, 1600),
    (UNHEX(REPLACE('2672c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), UNHEX(REPLACE('2222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), '2024-05-08', -1.0, 5125, 5075, 5200, 5050, 1500),
    (UNHEX(REPLACE('2289c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), UNHEX(REPLACE('2222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), '2024-05-09', 2.0, 5075, 5175, 5250, 5050, 1800),
    (UNHEX(REPLACE('2112c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), UNHEX(REPLACE('2222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), '2024-05-10', -1.5, 5175, 5100, 5200, 5075, 1700),
    (UNHEX(REPLACE('2822c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), UNHEX(REPLACE('2222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), '2024-05-11', 1.0, 5100, 5150, 5200, 5050, 1600),
    (UNHEX(REPLACE('2172c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), UNHEX(REPLACE('2222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), '2024-05-12', -0.5, 5150, 5125, 5200, 5100, 1500),
    (UNHEX(REPLACE('3122c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), UNHEX(REPLACE('2222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), '2024-05-13', 2.0, 5125, 5225, 5300, 5100, 1800),
    (UNHEX(REPLACE('4122c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), UNHEX(REPLACE('2222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), '2024-05-14', -1.0, 5225, 5175, 5250, 5150, 1700),
    (UNHEX(REPLACE('5232c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), UNHEX(REPLACE('2222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), '2024-05-15', 1.0, 5175, 5225, 5300, 5150, 1600),
    (UNHEX(REPLACE('3212c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), UNHEX(REPLACE('2222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), '2024-05-16', -0.5, 5225, 5200, 5250, 5150, 1500),
    (UNHEX(REPLACE('6542c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), UNHEX(REPLACE('2222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), '2024-05-17', 1.5, 5200, 5275, 5350, 5150, 1800),
    (UNHEX(REPLACE('2222c0f7-2c97-4bd7-a201-0de1392f1df0', '-', '')), UNHEX(REPLACE('2222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), '2024-05-18', -1.0, 5275, 5225, 5300, 5200, 1700),
    (UNHEX(REPLACE('2222c0f7-2c97-4bd7-a202-0de1392f1df0', '-', '')), UNHEX(REPLACE('2222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), '2024-05-19', 1.0, 5225, 5275, 5350, 5200, 1600),
    (UNHEX(REPLACE('2222c0f7-2c97-4bd7-a203-0de1392f1df0', '-', '')), UNHEX(REPLACE('2222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), '2024-05-20', 0.5, 5275, 5300, 5350, 5250, 1500),
    (UNHEX(REPLACE('2222c0f7-2c97-4bd7-a204-0de1392f1df0', '-', '')), UNHEX(REPLACE('2222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), '2024-05-21', -2.0, 5300, 5200, 5325, 5150, 1800),
    (UNHEX(REPLACE('2222c0f7-2c97-4bd7-a205-0de1392f1df0', '-', '')), UNHEX(REPLACE('2222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), '2024-05-22', 1.5, 5200, 5275, 5350, 5150, 1700),
    (UNHEX(REPLACE('2222c0f7-2c97-4bd7-a206-0de1392f1df0', '-', '')), UNHEX(REPLACE('2222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), '2024-05-23', 1.0, 5275, 5325, 5400, 5250, 1600),
    (UNHEX(REPLACE('2222c0f7-2c97-4bd7-a207-0de1392f1df0', '-', '')), UNHEX(REPLACE('2222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), '2024-05-24', -0.5, 5325, 5300, 5350, 5250, 1500),
    (UNHEX(REPLACE('2222c0f7-2c97-4bd7-a208-0de1392f1df0', '-', '')), UNHEX(REPLACE('2222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), '2024-05-25', 2.0, 5300, 5400, 5450, 5250, 1800),
    (UNHEX(REPLACE('2222c0f7-2c97-4bd7-a209-0de1392f1df0', '-', '')), UNHEX(REPLACE('2222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), '2024-05-26', -1.0, 5400, 5350, 5450, 5300, 1700),
    (UNHEX(REPLACE('2222c0f7-2c97-4bd7-a210-0de1392f1df0', '-', '')), UNHEX(REPLACE('2222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), '2024-05-27', 1.0, 5350, 5400, 5450, 5300, 1600),
    (UNHEX(REPLACE('2222c0f7-2c97-4bd7-a211-0de1392f1df0', '-', '')), UNHEX(REPLACE('2222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), '2024-05-28', -0.5, 5400, 5375, 5450, 5350, 1500),
    (UNHEX(REPLACE('2222c0f7-2c97-4bd7-a212-0de1392f1df0', '-', '')), UNHEX(REPLACE('2222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), '2024-05-29', 1.5, 5375, 5450, 5500, 5350, 1800),
    (UNHEX(REPLACE('2222c0f7-2c97-4bd7-a213-0de1392f1df0', '-', '')), UNHEX(REPLACE('2222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), '2024-05-30', -1.0, 5450, 5400, 5500, 5350, 1700),
    (UNHEX(REPLACE('2222c0f7-2c97-4bd7-a214-0de1392f1df0', '-', '')), UNHEX(REPLACE('2222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), '2024-05-31', 1.0, 5400, 5450, 5500, 5350, 1600),
    (UNHEX(REPLACE('2222c0f7-2c97-4bd7-a215-0de1392f1df0', '-', '')), UNHEX(REPLACE('2222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', '')), '2024-06-01', 0.5, 5450, 5475, 5550, 5400, 1500)
    ON DUPLICATE KEY UPDATE day_transaction_log_id = day_transaction_log_id;

-- user3

INSERT INTO users (user_id, email, password, phone_number, name, role, created_at, updated_at, wallet_address)
VALUES
    (UNHEX(REPLACE('2222c0f7-0c97-4bd7-a200-0de1392f1df2', '-', '')),'test3@user.com' , '$2a$04$PqAI9RkcXM3QK6A/GkpbCetMX5Bh7Mt9eV5vO/3ULVPPJwG7Vishi', '01012341234', 'test_user3', 'USER', NOW(), NOW(), 'r3roFB8dSpw6wPr4QH9JUKnXTaD628Zx9L')
    ON DUPLICATE KEY UPDATE user_id = user_id;

INSERT INTO accounts (account_id, user_id, deposit, average_earning_rate)
VALUES
    (UNHEX(REPLACE('6111c0f7-0c97-4bd7-a200-0de1392f1df2', '-', '')), UNHEX(REPLACE('2222c0f7-0c97-4bd7-a200-0de1392f1df2', '-', '')), 9999999999999999, 0.0)
    ON DUPLICATE KEY UPDATE account_id = account_id;
/*
 package org.example.first.groundingappapis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@Table(name = "inventorys")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {
    @Id
    @Column(name = "inventory_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID id;

    //수량, 가격, 수익률, 현재가
    @Column(name = "quantity")
    private Integer quantity;

    //매도 주문 가능 수량, 호가 등록시 -, 매도 주문 취소 시 +
    @Column(name = "sellable_quantity")
    private Integer sellableQuantity;

    //매입금액
    @Column(name = "average_buying_price")
    private Integer averageBuyingPrice;

    @Column(name = "earnings_rate")
    private Double earningsRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false, columnDefinition = "BINARY(16)", foreignKey = @ForeignKey(name = "fk_inventorys_account"))
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false, columnDefinition = "BINARY(16)", foreignKey = @ForeignKey(name = "fk_inventorys_property"))
    private Property property;

    @Builder
    public Inventory(Integer quantity, Integer averageBuyingPrice, Integer sellableQuantity, Double earningsRate) {
        this.quantity = quantity;
        this.averageBuyingPrice = averageBuyingPrice;
        this.sellableQuantity = sellableQuantity;
        this.earningsRate = earningsRate;
    }
*/
    -- user3이 처음에 3000개의 예시 임야를 5000원에 구매
-- INSERT INTO inventorys (inventory_id, quantity, sellable_quantity, average_buying_price, earnings_rate, account_id, property_id) VALUES
--     (UNHEX(REPLACE('1111c0f7-0c97-4bd7-a200-0de1392f1df0', '-', '')), 3000, 3000, 5000, 0.0, UNHEX(REPLACE('6111c0f7-0c97-4bd7-a200-0de1392f1df0', '-', '')), UNHEX(REPLACE('2222c0f7-2c97-4bd7-a200-0de1392f1df0', '-', ''))
--     ON DUPLICATE KEY UPDATE inventory_id = inventory_id;