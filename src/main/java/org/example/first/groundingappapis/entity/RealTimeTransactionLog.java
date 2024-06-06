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

    @Column(name = "executed_at", nullable = false, unique = true)
    private LocalDateTime executedAt;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "executed_price")
    private Integer executedPrice;

    @Column(name = "fluctuation_rate")
    private Double fluctuationRate;

    @PrePersist
    public void prePersist() {
        if (this.id == null)
            this.id = UUID.randomUUID();

        if (this.executedAt == null)
            this.executedAt = LocalDateTime.now();
    }

    @Builder
    public RealTimeTransactionLog(Property property, LocalDateTime executedAt, Integer quantity, Integer executedPrice, Double fluctuationRate, User user) {
        this.property = property;
        this.executedAt = executedAt;
        this.quantity = quantity;
        this.executedPrice = executedPrice;
        this.fluctuationRate = fluctuationRate;
    }

    public RealTimeTransactionLogDto toDto() {
        return RealTimeTransactionLogDto.builder()
                .executedAt(executedAt)
                .quantity(quantity)
                .executedPrice(executedPrice)
                .fluctuationRate(fluctuationRate)
                .build();
    }

    public void updateProperty(Property property) {
        this.property = property;
        property.getRealTimeTransactionLogs().add(this);
    }

    public UUID getPropertyId() {
        return property.getId();
    }
}
