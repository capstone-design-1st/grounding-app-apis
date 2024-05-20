package org.example.first.groundingappapis.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class QuoteDto {

    private Integer dayMaxPrice;
    private Integer dayMinPrice;
    private Integer presentPrice;

    @Builder
    public QuoteDto(Integer dayMaxPrice,
                    Integer dayMinPrice,
                    Integer presentPrice) {
        this.dayMaxPrice = dayMaxPrice != null ? dayMaxPrice : 0;
        this.dayMinPrice = dayMinPrice != null ? dayMinPrice : 0;
        this.presentPrice = presentPrice != null ? presentPrice : 0;
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
}
