package org.example.first.groundingappapis.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RealTimeTransactionLogDto {
    private UUID propertyId;
    private LocalDateTime executedAt;
    private Integer quantity;
    private Integer executedPrice;
    private Double fluctuationRate;
    @Builder
    public RealTimeTransactionLogDto(UUID propertyId, LocalDateTime executedAt, Integer quantity, Integer executedPrice, Double fluctuationRate) {
        this.propertyId = propertyId;
        this.executedAt = executedAt != null ? executedAt : LocalDateTime.now();
        this.quantity = quantity != null ? quantity : 0;
        this.executedPrice = executedPrice != null ? executedPrice : 0;
        this.fluctuationRate = fluctuationRate != null ? fluctuationRate : 0.0;
    }

    @Data
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ReadResponse{
        private UUID propertyId;
        private Time executedAt;
        private Integer quantity;
        private Integer executedPrice;
        private Double fluctuationRate;

        @Builder
        public ReadResponse(UUID propertyId, LocalDateTime executedAt, Integer quantity, Integer executedPrice, Double fluctuationRate) {
            this.propertyId = propertyId;
            this.executedAt = executedAt != null ? Time.valueOf(LocalDateTime.now().toLocalTime()) : Time.valueOf(LocalDateTime.now().toLocalTime());
            this.quantity = quantity != null ? quantity : 0;
            this.executedPrice = executedPrice != null ? executedPrice : 0;
            this.fluctuationRate = fluctuationRate != null ? fluctuationRate : 0.0;
        }
    }
}
