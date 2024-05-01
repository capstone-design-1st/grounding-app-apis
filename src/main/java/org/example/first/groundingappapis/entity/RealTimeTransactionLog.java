package org.example.first.groundingappapis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "real_time_transaction_logs")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RealTimeTransactionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "real_time_transaction_log_id")
    private Long realTimeTransactionLogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @Column(name = "executed_at", nullable = false)
    private LocalDateTime executedAt;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "executed_price")
    private Integer executedPrice;

    @Column(name = "fluctuation_rate")
    private Double fluctuationRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public RealTimeTransactionLog(Property property, LocalDateTime executedAt, Integer amount, Integer executedPrice, Double fluctuationRate, User user) {
        this.property = property;
        this.executedAt = executedAt;
        this.amount = amount;
        this.executedPrice = executedPrice;
        this.fluctuationRate = fluctuationRate;
        this.user = user;
    }
}
