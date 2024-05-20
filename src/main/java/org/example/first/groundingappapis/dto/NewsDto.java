package org.example.first.groundingappapis.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NewsDto {
    private String title;
    private String thumbnailUrl;
    private LocalDate reportedAt;
    private String publisher;
    private String issuer;

    @Builder
    public NewsDto(String title, String thumbnailUrl, LocalDate reportedAt, String publisher, String issuer) {
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.reportedAt = reportedAt;
        this.publisher = publisher;
        this.issuer = issuer;
    }

    @Data
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class GetDetailResponse{
        private String title;
        private String content;
        private String thumbnailUrl;
        private LocalDate reportedAt;
        private String publisher;
        private String issuer;

        @Builder
        public GetDetailResponse(String title, String content, String thumbnailUrl, LocalDate reportedAt, String publisher, String issuer) {
            this.title = title;
            this.content = content;
            this.thumbnailUrl = thumbnailUrl;
            this.reportedAt = reportedAt;
            this.publisher = publisher;
            this.issuer = issuer;
        }
    }
}
