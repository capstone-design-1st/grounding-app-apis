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
    private Double AverageEarningRate;
    @Builder
    public AccountDto(Long deposit, Double averageEarningRate) {
        this.deposit = deposit != null ? deposit : 0L;
        this.AverageEarningRate = averageEarningRate != null ? averageEarningRate : 0.0;
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
        private Integer presentPrice;
        private Integer evaluationPrice;
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
                                        Integer presentPrice,
                                        Integer differenceAmount,
                                        Double fluctuationRate,
                                        Integer totalBuyingPrice) {
            this.propertyId = propertyId != null ? propertyId : "";
            this.propertyName = propertyName != null ? propertyName : "";
            this.type = type != null ? type : "";
            this.quantity = quantity != null ? quantity : 0;
            this.averageBuyingPrice = averageBuyingPrice != null ? averageBuyingPrice : 0;
            this.presentPrice = presentPrice != null ? presentPrice : 0;
            this.evaluationPrice = evaluationPrice != null ? evaluationPrice : 0;
            this.differenceAmount = differenceAmount != null ? differenceAmount : 0;
            this.fluctuationRate = fluctuationRate != null ? fluctuationRate : 0.0;
            this.totalBuyingPrice = totalBuyingPrice != null ? totalBuyingPrice : 0;
        }
    }
    @Data
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ReadCompletedOrderResponse {
        private UUID propertyId;
        private String propertyName;
        private Integer quantity;
        private LocalDate date;
        private String type; //매수, 매도
        private Integer price;
        private String propertyType;

        @Builder
        public ReadCompletedOrderResponse(UUID propertyId,
                                       String propertyName,
                                       Integer quantity,
                                       LocalDateTime dateTime,
                                       String type,
                                       Integer price,
                                       String propertyType) {
            this.propertyId = propertyId != null ? propertyId : UUID.randomUUID();
            this.propertyName = propertyName != null ? propertyName : "";
            this.quantity = quantity != null ? quantity : 0;
            this.date = dateTime != null ? dateTime.toLocalDate() : LocalDate.now();
            this.type = type != null ? type : "";
            this.price = price != null ? price : 0;
            this.propertyType = propertyType != null ? propertyType : "";
        }
    }
    @Data
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ReadPresentStatusResponse {
        private UUID userId;
        //매입금액
        private Long totalBuyingPrice;
        //조각모집중인금액
        private Long fundraisingPrice;
        //예수금
        private Long deposit;
        //총수익률
        private Double averageEarningRate;
        //평가금액
        private Integer evaluationPrice;
        //평가손익
        private Integer evaluationEarning;

        @Builder
        public ReadPresentStatusResponse(UUID userId,
                                          Long totalBuyingPrice,
                                          Long fundraisingPrice,
                                          Long deposit,
                                          Double averageEarningRate,
                                          Integer evaluationPrice,
                                          Integer evaluationEarning) {
            this.userId = userId;
            this.totalBuyingPrice = totalBuyingPrice != null ? totalBuyingPrice : 0L;
            this.fundraisingPrice = fundraisingPrice != null ? fundraisingPrice : 0L;
            this.deposit = deposit != null ? deposit : 0L;
            this.averageEarningRate = averageEarningRate != null ? averageEarningRate : 0.0;
            this.evaluationPrice = evaluationPrice != null ? evaluationPrice : 0;
            this.evaluationEarning = evaluationEarning != null ? evaluationEarning : 0;
        }
    }
}
