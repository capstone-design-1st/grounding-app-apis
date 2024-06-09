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
public class QuoteDto {

    private Integer price;
    private Integer quantity;
    private String type;
    @Builder
    public QuoteDto(Integer price,
                    Integer quantity,
                    String type) {

        this.price = price != null ? price : 0;
        this.quantity = quantity != null ? quantity : 0;
        this.type = type != null ? type : "";
    }

    @Data
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class UploadOrderResponse{
        private LocalDateTime createdAt;
        private String userId;
        private String propertyId;
        @Builder
        public UploadOrderResponse(LocalDateTime createdAt,
                                    String userId,
                                    String propertyId) {
            this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
            this.userId = userId != null ? userId : "";
            this.propertyId = propertyId != null ? propertyId : "";
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class GetResponse{}
    @Data
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ReadResponse{
        private Integer price;
        private Long quantity;
        private String type;

        @Builder
        public ReadResponse(Integer price,
                            Long quantity,
                            String type) {
            this.price = price != null ? price : 0;
            this.quantity = quantity != null ? quantity : 0L;
            this.type = type != null ? type : "";
        }
    }

}
