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
import java.util.UUID;

@Data
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DayTransactionLogDto {

    private UUID propertyId;

    private LocalDate date;
    //변동률
    private Double fluctuationRate;
    //시가
    private Integer openingPrice;
    //종가
    private Integer closingPrice;
    //최고가
    private Integer maxPrice;
    //최저가
    private Integer minPrice;
    //거래량
    private Long volumeCount;

    @Builder
    public DayTransactionLogDto(
                                UUID propertyId,
                                LocalDate date,
                                Double fluctuationRate,
                                Integer openingPrice,
                                Integer closingPrice,
                                Integer maxPrice,
                                Integer minPrice,
                                Long volumeCount) {
        this.propertyId = propertyId;
        this.date = date != null ? date : LocalDate.now();
        this.fluctuationRate = fluctuationRate != null ? fluctuationRate : 0.0;
        this.openingPrice = openingPrice != null ? openingPrice : 0;
        this.closingPrice = closingPrice != null ? closingPrice : 0;
        this.maxPrice = maxPrice != null ? maxPrice : 0;
        this.minPrice = minPrice != null ? minPrice : 0;
        this.volumeCount = volumeCount != null ? volumeCount : 0L;
    }

    /*
        public DayTransactionLogDto toDto() {
        return DayTransactionLogDto.builder()
                .propertyId(property.getId())
                .date(this.date)
                .fluctuationRate(this.fluctuationRate)
                .openingPrice(this.openingPrice)
                .closingPrice(this.closingPrice)
                .maxPrice(this.maxPrice)
                .minPrice(this.minPrice)
                .volumeCount(this.volumeCount)
                .build();
    }
     */
    @Data
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ReadResponse{
        private UUID propertyId;
        private LocalDate date;
        private Double fluctuationRate;
        private Integer openingPrice;
        private Integer closingPrice;
        private Integer maxPrice;
        private Integer minPrice;
        private Long volumeCount;

        @Builder
        public ReadResponse(UUID propertyId, LocalDate date, Double fluctuationRate, Integer openingPrice, Integer closingPrice, Integer maxPrice, Integer minPrice, Long volumeCount) {
            this.propertyId = propertyId;
            this.date = date != null ? date : LocalDate.now();
            this.fluctuationRate = fluctuationRate != null ? fluctuationRate : 0.0;
            this.openingPrice = openingPrice != null ? openingPrice : 0;
            this.closingPrice = closingPrice != null ? closingPrice : 0;
            this.maxPrice = maxPrice != null ? maxPrice : 0;
            this.minPrice = minPrice != null ? minPrice : 0;
            this.volumeCount = volumeCount != null ? volumeCount : 0L;
        }
    }
}
