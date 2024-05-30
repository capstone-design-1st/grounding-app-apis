package org.example.first.groundingappapis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@Table(name = "inventorys")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {
    @Id
    @Column(name = "inventory_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID id;

    //수량, 가격, 수익률, 현재가
    @Column(name = "quantity")
    private Integer quantity;

    //매입금액
    @Column(name = "average_buying_price")
    private Integer averageBuyingPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false, columnDefinition = "BINARY(16)", foreignKey = @ForeignKey(name = "fk_inventorys_account"))
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false, columnDefinition = "BINARY(16)", foreignKey = @ForeignKey(name = "fk_inventorys_property"))
    private Property property;

    @Builder
    public Inventory(Integer quantity, Integer averageBuyingPrice) {
        this.quantity = quantity;
        this.averageBuyingPrice = averageBuyingPrice;
    }

    public void updateProperty(Property property) {
        this.property = property;
    }

    public void updateAccount(Account account) {
        this.account = account;
        account.getInventories().add(this);
    }

    @PrePersist
    public void prePersist() {
        if (this.id == null)
            this.id = UUID.randomUUID();
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setAverageBuyingPrice(Integer averageBuyingPrice) {
        this.averageBuyingPrice = averageBuyingPrice;
    }


//    public InventoryDto toDto() {
//        return InventoryDto.builder()
//                .build();
//    }
}
