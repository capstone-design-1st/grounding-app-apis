package org.example.first.groundingappapis.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RealTimeTransactionLogDto {

    private LocalDateTime executedAt;
    private Integer amount;
    private Integer executedPrice;
    private Double fluctuationRate;
    @Builder
    public RealTimeTransactionLogDto(LocalDateTime executedAt, Integer amount, Integer executedPrice, Double fluctuationRate) {
        this.executedAt = executedAt != null ? executedAt : LocalDateTime.now();
        this.amount = amount != null ? amount : 0;
        this.executedPrice = executedPrice != null ? executedPrice : 0;
        this.fluctuationRate = fluctuationRate != null ? fluctuationRate : 0.0;
    }

    public static class ReadResponse{}
}
