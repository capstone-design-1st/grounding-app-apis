package org.example.first.groundingappapis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.first.groundingappapis.dto.OrderDto;

import java.time.LocalDateTime;
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
        if (this.createdAt == null)
            this.createdAt = LocalDateTime.now();
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

    //청약중, 체결 대기중, 체결 완료, 취소
    @Column(name = "status", length = 10)
    private String status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "BINARY(16)", foreignKey = @ForeignKey(name = "fk_orders_user"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false, columnDefinition = "BINARY(16)", foreignKey = @ForeignKey(name = "fk_orders_property"))
    private Property property;

    @Builder
    public Order(String type, Integer price, Integer quantity, User user, Property property, String status, LocalDateTime createdAt) {
        this.type = type;
        this.price = price;
        this.quantity = quantity;
        this.user = user;
        this.property = property;
        this.status = status;
        this.createdAt = createdAt;
    }

    public OrderDto toDto() {
        return OrderDto.builder()
                .id(id)
                .type(type)
                .price(price)
                .quantity(quantity)
                .status(status)
                .createdAt(createdAt)
                .build();
    }

    public void updateUser(User user) {
        this.user = user;
    }

    public void updateProperty(Property property) {
        this.property = property;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void updateStatus(String status) {
        this.status = status;
    }

    public void updateQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}

