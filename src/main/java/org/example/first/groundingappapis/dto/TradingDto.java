package org.example.first.groundingappapis.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class TradingDto {
    @Data
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class BuyRequest {
        private String propertyId;
        private int quantity;
        private int price;

        @Builder
        public BuyRequest(String propertyId, int quantity, int price) {
            this.propertyId = propertyId;
            this.quantity = quantity;
            this.price = price;
        }
    }

    @Data
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class SellRequest {
        private String propertyId;
        private int quantity;
        private int price;

        @Builder
        public SellRequest(String propertyId, int quantity) {
            this.propertyId = propertyId;
            this.quantity = quantity;
            this.price = price;
        }
    }
}
