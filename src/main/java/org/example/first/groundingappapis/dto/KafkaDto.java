package org.example.first.groundingappapis.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class KafkaDto {
    @Getter
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class NotificationProducingDto {
        /*
                this.pieceInvestment = pieceInvestment;
        this.userName = userName;
        this.quantity = quantity;
        this.progressRate = progressRate;
        this.notificationTime = notificationTime;
         */
        private String pieceInvestmentName;
        private String userName;
        private Integer quantity;
        private Double progressRate;
        private LocalDateTime executedTime;

        @Builder
        public NotificationProducingDto(String pieceInvestmentName, String userName, Integer quantity, Double progressRate, LocalDateTime executedTime) {
            this.pieceInvestmentName = pieceInvestmentName;
            this.userName = userName;
            this.quantity = quantity;
            this.progressRate = progressRate;
            this.executedTime = executedTime;
        }
    }
}
