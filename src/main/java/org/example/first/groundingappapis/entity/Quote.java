package org.example.first.groundingappapis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.first.groundingappapis.dto.QuoteDto;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

//호가
@Entity
@Table(name = "quotes")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Quote {

    @Id
    @Column(name = "quote_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false, columnDefinition = "BINARY(16)", foreignKey = @ForeignKey(name = "fk_quotes_property"))
    private Property property;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false, columnDefinition = "BINARY(16)", foreignKey = @ForeignKey(name = "fk_quotes_account"))
    private Account account;

    @PrePersist
    public void prePersist() {
        this.createdAt = (this.createdAt == null) ? LocalDateTime.now() : this.createdAt;
        if (this.id == null)
            this.id = UUID.randomUUID();
    }

    @Builder
    public Quote(Property property, LocalDateTime createdAt, Integer quantity, Integer price, Account account) {
        this.property = property;
        this.createdAt = createdAt;
        this.quantity = quantity;
        this.price = price;
        this.account = account;
    }

    public QuoteDto toDto() {
        return QuoteDto.builder()
                .createdAt(createdAt)
                .quantity(quantity)
                .price(price)
                .build();
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void updateProperty(Property property) {
        this.property = property;
        property.getQuotes().add(this);
    }

    public void updateAccount(Account account) {
        this.account = account;
        account.getQuotes().add(this);
    }
}
