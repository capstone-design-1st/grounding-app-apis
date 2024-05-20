package org.example.first.groundingappapis.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.first.groundingappapis.entity.Property;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DayTransactionLogDto {
    private LocalDate date;
    private Integer amount;
    private Integer executedPrice;
    private Double fluctuationRate;
    private Integer openingPrice;
    private Integer closingPrice;

    @Builder
    public DayTransactionLogDto(LocalDate date,
                                Integer amount,
                                Integer executedPrice,
                                Double fluctuationRate,
                                Integer openingPrice,
                                Integer closingPrice) {
        this.date = date != null ? date : LocalDate.now();
        this.amount = amount != null ? amount : 0;
        this.executedPrice = executedPrice != null ? executedPrice : 0;
        this.fluctuationRate = fluctuationRate != null ? fluctuationRate : 0.0;
        this.openingPrice = openingPrice != null ? openingPrice : 0;
        this.closingPrice = closingPrice != null ? closingPrice : 0;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ReadResponse{}
}
