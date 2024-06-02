package org.example.first.groundingappapis.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

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
    @Data
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ReadTransactionResponse {
        private UUID propertyId;
        private String propertyName;
        private Integer quantity;
        private LocalDate date;
        private String type;
        private Integer price;

        @Builder
        public ReadTransactionResponse(UUID propertyId,
                                       String propertyName,
                                       Integer quantity,
                                       LocalDateTime dateTime,
                                       String type,
                                       Integer price) {
            this.propertyId = propertyId != null ? propertyId : UUID.randomUUID();
            this.propertyName = propertyName != null ? propertyName : "";
            this.quantity = quantity != null ? quantity : 0;
            this.date = dateTime != null ? dateTime.toLocalDate() : LocalDate.now();
            this.type = type != null ? type : "";
            this.price = price != null ? price : 0;
        }
    }
}
