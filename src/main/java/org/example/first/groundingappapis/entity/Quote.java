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
@Table(name = "quotes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"property_id", "order_id", "created_at"})
})
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Quote {

    @Id
    @Column(name = "quote_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", columnDefinition = "BINARY(16)", foreignKey = @ForeignKey(name = "fk_quotes_property"))
    private Property property;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private Integer price;

    @Column(name = "type", nullable = false)
    private String type;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", columnDefinition = "BINARY(16)", foreignKey = @ForeignKey(name = "fk_quotes_order"))
    private Order order;

    @PrePersist
    public void prePersist() {
        this.createdAt = (this.createdAt == null) ? LocalDateTime.now() : this.createdAt;
        if (this.id == null)
            this.id = UUID.randomUUID();
    }

    @Builder
    public Quote(Property property, LocalDateTime createdAt, Integer quantity, String type, Integer price) {
        this.property = property;
        this.createdAt = createdAt;
        this.quantity = quantity;
        this.price = price;
        this.type = type;
    }

    public QuoteDto toDto() {
        return QuoteDto.builder()
                .quantity(quantity)
                .price(price)
                .type(type)
                .build();
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void updateProperty(Property property) {
        this.property = property;
        property.getQuotes().add(this);
    }

    public void updateOrder(Order order) {
        this.order = order;
        if (order != null)
            order.setQuote(this);
    }

    public void updateQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void updateType(String type) {
        this.type = type;
    }

    public void increaseQuantity(int executedQuantity) {
        this.quantity += executedQuantity;
    }
    public void decreaseQuantity(int executedQuantity) {
        this.quantity -= executedQuantity;
    }

    public void freeQuote() {
        this.order = null;
        this.property = null;
    }
}
