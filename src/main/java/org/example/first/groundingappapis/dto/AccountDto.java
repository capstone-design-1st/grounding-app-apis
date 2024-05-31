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
@JsonNaming
public class AccountDto {

    private Long deposit;
    private Double totalEarningRate;
    @Builder
    public AccountDto(Long deposit, Double totalEarningRate) {
        this.deposit = deposit != null ? deposit : 0L;
        this.totalEarningRate = totalEarningRate != null ? totalEarningRate : 0.0;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class GetResponse{}

    @Data
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ReadUserPropertyResponse {
        private String propertyId;
        private String propertyName;
        private String type;
        private Integer quantity;
        private Integer averageBuyingPrice;
        private Integer evaluationPrice; // 현재가
        private Integer differenceAmount;
        private Double fluctuationRate;
        private Integer totalBuyingPrice;

        @Builder
        public ReadUserPropertyResponse(String propertyId,
                                        String propertyName,
                                        String type,
                                        Integer quantity,
                                        Integer averageBuyingPrice,
                                        Integer evaluationPrice,
                                        Integer differenceAmount,
                                        Double fluctuationRate,
                                        Integer totalBuyingPrice) {
            this.propertyId = propertyId != null ? propertyId : "";
            this.propertyName = propertyName != null ? propertyName : "";
            this.type = type != null ? type : "";
            this.quantity = quantity != null ? quantity : 0;
            this.averageBuyingPrice = averageBuyingPrice != null ? averageBuyingPrice : 0;
            this.evaluationPrice = evaluationPrice != null ? evaluationPrice : 0;
            this.differenceAmount = differenceAmount != null ? differenceAmount : 0;
            this.fluctuationRate = fluctuationRate != null ? fluctuationRate : 0.0;
            this.totalBuyingPrice = totalBuyingPrice != null ? totalBuyingPrice : 0;
        }
    }
}
