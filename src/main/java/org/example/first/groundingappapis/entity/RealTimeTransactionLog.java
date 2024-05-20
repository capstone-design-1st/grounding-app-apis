package org.example.first.groundingappapis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.first.groundingappapis.dto.RealTimeTransactionLogDto;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "real_time_transaction_logs")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RealTimeTransactionLog {

    @Id
    @Column(name = "real_time_transaction_log_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false, columnDefinition = "BINARY(16)", foreignKey = @ForeignKey(name = "fk_real_time_transaction_logs_property"))
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

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = (this.createdAt == null) ? LocalDateTime.now() : this.createdAt;
        if (this.id == null)
            this.id = UUID.randomUUID();
    }

    @Builder
    public RealTimeTransactionLog(Property property, LocalDateTime executedAt, Integer amount, Integer executedPrice, Double fluctuationRate, User user) {
        this.property = property;
        this.executedAt = executedAt;
        this.amount = amount;
        this.executedPrice = executedPrice;
        this.fluctuationRate = fluctuationRate;
        this.user = user;
    }

    public RealTimeTransactionLogDto toDto() {
        return RealTimeTransactionLogDto.builder()
                .executedAt(executedAt)
                .amount(amount)
                .executedPrice(executedPrice)
                .fluctuationRate(fluctuationRate)
                .build();
    }
}
