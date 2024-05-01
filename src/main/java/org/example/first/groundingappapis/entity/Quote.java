package org.example.first.groundingappapis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "quotes")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "day_max_price")
    private Integer dayMaxPrice;

    @Column(name = "day_min_price")
    private Integer dayMinPrice;

    @Column(name = "present_price")
    private Integer presentPrice;

    @Builder
    public Quote(Property property, LocalDateTime createdAt, Integer dayMaxPrice, Integer dayMinPrice, Integer presentPrice) {
        this.property = property;
        this.createdAt = createdAt;
        this.dayMaxPrice = dayMaxPrice;
        this.dayMinPrice = dayMinPrice;
        this.presentPrice = presentPrice;
    }
}
