package org.example.first.groundingappapis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.first.groundingappapis.dto.OrderDto;

import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @Column(name = "order_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID id;

    @PrePersist
    public void prePersist() {
        if (this.id == null)
            this.id = UUID.randomUUID();
    }
    //매수/매도
    @Column(name = "type", length = 10)
    private String type;

    //개별 가격
    @Column(name = "price")
    private Integer price;

    //수량
    @Column(name = "quantity")
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "BINARY(16)", foreignKey = @ForeignKey(name = "fk_orders_user"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false, columnDefinition = "BINARY(16)", foreignKey = @ForeignKey(name = "fk_orders_property"))
    private Property property;

    @Builder
    public Order(String type, Integer price, Integer quantity, User user, Property property) {
        this.type = type;
        this.price = price;
        this.quantity = quantity;
        this.user = user;
        this.property = property;
    }

    public OrderDto toDto() {
        return OrderDto.builder()
                .id(id)
                .type(type)
                .price(price)
                .quantity(quantity)
                .build();
    }
}

