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
        private int quantity;
        private int price;

        @Builder
        public BuyRequest(String propertyId, int quantity, int price) {
            this.quantity = quantity;
            this.price = price;
        }
    }

    @Data
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class SellRequest {
        private int quantity;
        private int price;

        @Builder
        public SellRequest(int quantity, int price) {
            this.quantity = quantity;
            this.price = price;
        }
    }
}
