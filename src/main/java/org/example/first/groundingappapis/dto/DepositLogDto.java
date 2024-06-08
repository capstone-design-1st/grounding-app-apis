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
public class DepositLogDto {

    private String type;
    private Long amount;

    @Builder
    public DepositLogDto(String type,
                         Long amount) {
        this.type = type != null ? type : "";
        this.amount = amount != null ? amount : 0;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ReadResponse{
        private Long amount;
        private String type;
        private LocalDateTime createdAt;

        @Builder
        public ReadResponse(Long amount, String type, LocalDateTime createdAt) {
            this.amount = amount != null ? amount : 0L;
            this.type = type != null ? type : "";
            this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        }
    }
}
