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
}
