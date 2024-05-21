package org.example.first.groundingappapis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.first.groundingappapis.dto.DayTransactionLogDto;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "day_transaction_logs")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DayTransactionLog {

    @Id
    @Column(name = "day_transaction_log_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID id;

    @PrePersist
    public void prePersist() {
        this.date = (this.date == null) ? LocalDate.now() : this.date;
        if (this.id == null)
            this.id = UUID.randomUUID();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false, columnDefinition = "BINARY(16)", foreignKey = @ForeignKey(name = "fk_day_transaction_logs_property"))
    private Property property;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "amount")
    private Integer amount;

    //변동률
    @Column(name = "fluctuation_rate")
    private Double fluctuationRate;

    @Column(name = "opening_price")
    private Integer openingPrice;

    @Column(name = "closing_price")
    private Integer closingPrice;

    @Column(name = "max_price")
    private Integer maxPrice;

    @Column(name = "min_price")
    private Integer minPrice;

    @Builder
    public DayTransactionLog(Property property, LocalDate date, Double fluctuationRate, Integer openingPrice, Integer closingPrice, Integer maxPrice, Integer minPrice) {
        this.property = property;
        this.date = date;
        this.fluctuationRate = fluctuationRate;
        this.openingPrice = openingPrice;
        this.closingPrice = closingPrice;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
    }

    public DayTransactionLogDto toDto() {
        return DayTransactionLogDto.builder()
                .propertyId(property.getId())
                .date(this.date)
                .fluctuationRate(this.fluctuationRate)
                .openingPrice(this.openingPrice)
                .closingPrice(this.closingPrice)
                .maxPrice(this.maxPrice)
                .minPrice(this.minPrice)
                .build();
    }
}
