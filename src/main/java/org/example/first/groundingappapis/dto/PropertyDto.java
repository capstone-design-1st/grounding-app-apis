package org.example.first.groundingappapis.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PropertyDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ReadResponse {
        private UUID uuid;
        private String name;
        private Integer price;
        private Integer pieceCount;
        private Integer piecePrice;
        private Long viewCount;
        private Long likeCount;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private ThumbnailUrlDto thumbnailUrlDto;
        private ListingInformation listingInformation;

        public ReadResponse(UUID uuid, String name, Integer price, Integer pieceCount, Integer piecePrice, Long viewCount, Long likeCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
            this.uuid = uuid;
            this.name = name;
            this.price = price;
            this.pieceCount = pieceCount;
            this.piecePrice = piecePrice;
            this.viewCount = viewCount;
            this.likeCount = likeCount;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }
    }
}
