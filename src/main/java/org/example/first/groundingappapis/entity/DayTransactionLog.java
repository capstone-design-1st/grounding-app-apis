package org.example.first.groundingappapis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "day_transaction_logs")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DayTransactionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "day_transaction_log_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "executed_price")
    private Integer executedPrice;

    @Column(name = "fluctuation_rate")
    private Double fluctuationRate;

    @Column(name = "opening_price")
    private Integer openingPrice;

    @Column(name = "closing_price")
    private Integer closingPrice;

    @PrePersist
    public void prePersist() {
        this.date = (this.date == null) ? LocalDate.now() : this.date;
    }

    @Builder
    public DayTransactionLog(Property property, LocalDate date, Integer amount, Integer executedPrice, Double fluctuationRate, Integer openingPrice, Integer closingPrice) {
        this.property = property;
        this.date = date;
        this.amount = amount;
        this.executedPrice = executedPrice;
        this.fluctuationRate = fluctuationRate;
        this.openingPrice = openingPrice;
        this.closingPrice = closingPrice;
    }
}
