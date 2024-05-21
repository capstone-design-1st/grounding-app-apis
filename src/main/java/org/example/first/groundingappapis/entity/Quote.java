//package org.example.first.groundingappapis.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import org.example.first.groundingappapis.dto.QuoteDto;
//import org.springframework.data.annotation.CreatedDate;
//
//import java.time.LocalDateTime;
//import java.util.UUID;
//
////호가
//@Entity
//@Table(name = "quotes")
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
//public class Quote { //필요한가?
//
//    @Id
//    @Column(name = "quote_id", columnDefinition = "BINARY(16)", nullable = false)
//    private UUID id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "property_id", nullable = false, columnDefinition = "BINARY(16)", foreignKey = @ForeignKey(name = "fk_quotes_property"))
//    private Property property;
//
//
//    @CreatedDate
//    @Column(name = "created_at", nullable = false)
//    private LocalDateTime createdAt;
//
//    //일별, quote 둘 다 있는게 좋을듯
//    @Column(name = "day_max_price")
//    private Integer dayMaxPrice;
//
//    @Column(name = "day_min_price")
//    private Integer dayMinPrice;
//
//    @Column(name = "present_price")
//    private Integer presentPrice;
//
//    @PrePersist
//    public void prePersist() {
//        this.createdAt = (this.createdAt == null) ? LocalDateTime.now() : this.createdAt;
//        if (this.id == null)
//            this.id = UUID.randomUUID();
//    }
//
//    @Builder
//    public Quote(Property property, LocalDateTime createdAt, Integer dayMaxPrice, Integer dayMinPrice, Integer presentPrice) {
//        this.property = property;
//        this.createdAt = createdAt;
//        this.dayMaxPrice = dayMaxPrice;
//        this.dayMinPrice = dayMinPrice;
//        this.presentPrice = presentPrice;
//    }
//
//    public QuoteDto toDto() {
//        return QuoteDto.builder()
//                .dayMaxPrice(this.dayMaxPrice)
//                .dayMinPrice(this.dayMinPrice)
//                .presentPrice(this.presentPrice)
//                .build();
//    }
//}
