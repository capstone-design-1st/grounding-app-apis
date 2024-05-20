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
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NewsDto {

    private String s3Url;
    private String cloudfrontUrl;
    private String title;
    private String content;
    private LocalDate reportedAt;
    private String publisher;
    private String url;


    @Builder
    public NewsDto(String s3Url, String cloudfrontUrl, String title, String content, LocalDate reportedAt, String publisher, String url) {
        this.s3Url = s3Url;
        this.cloudfrontUrl = cloudfrontUrl;
        this.title = title;
        this.content = content;
        this.reportedAt = reportedAt;
        this.publisher = publisher;
        this.url = url;
    }

}
