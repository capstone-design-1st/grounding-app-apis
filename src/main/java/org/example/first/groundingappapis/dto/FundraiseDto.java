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
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FundraiseDto {
    private Double progressRate;
    private Integer progressAmount;
    private Integer investorCount;
    private String securityType;
    private String issuer;
    private Integer securityCount;
    private Integer issuePrice;
    private Integer totalFund;
    private LocalDate subscriptionStartDate;
    private LocalDate subscriptionEndDate;
    private String operatorName;
    private String operatorIntroduction;

    @Builder
    public FundraiseDto(Double progressRate,
                        Integer progressAmount,
                        Integer investorCount,
                        String securityType,
                        String issuer,
                        Integer securityCount,
                        Integer issuePrice,
                        Integer totalFund,
                        LocalDate subscriptionStartDate,
                        LocalDate subscriptionEndDate,
                        String operatorName,
                        String operatorIntroduction) {
        this.progressRate = progressRate;
        this.progressAmount = progressAmount;
        this.investorCount = investorCount;
        this.securityType = securityType;
        this.issuer = issuer;
        this.securityCount = securityCount;
        this.issuePrice = issuePrice;
        this.totalFund = totalFund;
        this.subscriptionStartDate = subscriptionStartDate;
        this.subscriptionEndDate = subscriptionEndDate;
        this.operatorName = operatorName;
        this.operatorIntroduction = operatorIntroduction;
    }

    @Data
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class FundraiseRequest{

        private Integer quantity;

        @Builder
        public FundraiseRequest(Integer quantity) {
            this.quantity = quantity;
        }
    }

    @Data
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class FundraiseResponse {
        private String userId;
        private String propertyId;
        private Integer quantity;
        private Integer price;
        private LocalDateTime createdAt;

        @Builder
        public FundraiseResponse(String userId, String propertyId, Integer quantity, Integer price, LocalDateTime createdAt) {
            this.userId = userId;
            this.propertyId = propertyId;
            this.quantity = quantity;
            this.price = price;
            this.createdAt = createdAt;
        }
    }
}
