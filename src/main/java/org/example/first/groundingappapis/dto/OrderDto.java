package org.example.first.groundingappapis.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderDto {
    private UUID id;
    private String type;
    private Integer price;
    private Integer quantity;
    private String status;
    private LocalDateTime createdAt;

    @Builder
    public OrderDto(UUID id, String type, Integer price, Integer quantity, String status, LocalDateTime createdAt) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
        this.createdAt = createdAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class GetResponse{}
    @Data
    @Builder
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ReadResponse{}

    @Data
    @Builder
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class GetTotalPriceResponse{
        private String propertyId;
        private Integer totalOrderPrice;
        private LocalDateTime createdAt;
        @Builder
        public GetTotalPriceResponse(String propertyId, Integer totalOrderPrice, LocalDateTime createdAt) {
            this.propertyId = propertyId;
            this.totalOrderPrice = totalOrderPrice;
            this.createdAt = createdAt;
        }
    }

    @Data
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class GetQuantityResponse {
        private String propertyId;
        private Integer quantity;
        private LocalDateTime createdAt;

        @Builder
        public GetQuantityResponse(String propertyId, Integer quantity, LocalDateTime createdAt) {
            this.propertyId = propertyId;
            this.quantity = quantity;
            this.createdAt = createdAt;
        }
    }

    @Data
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class GetQuantityOfInventoryResponse {
        private String propertyId;
        private Integer quantity;
        private LocalDateTime createdAt;

        @Builder
        public GetQuantityOfInventoryResponse(String propertyId, Integer quantity, LocalDateTime createdAt) {
            this.propertyId = propertyId;
            this.quantity = quantity;
            this.createdAt = createdAt;
        }
    }
}
