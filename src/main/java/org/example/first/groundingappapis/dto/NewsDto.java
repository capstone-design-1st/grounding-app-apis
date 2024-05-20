package org.example.first.groundingappapis.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NewsDto {

    private UUID id;
    private String s3Url;
    private String cloudfrontUrl;
    private String title;
    private String content;
    private LocalDate reportedAt;
    private String publisher;
    private String url;


    @Builder
    public NewsDto(UUID id, String s3Url, String cloudfrontUrl, String title, String content, LocalDate reportedAt, String publisher, String url) {
        this.id = id;
        this.s3Url = s3Url != null ? s3Url : "";
        this.cloudfrontUrl = cloudfrontUrl != null ? cloudfrontUrl : "";
        this.title = title != null ? title : "";
        this.content = content != null ? content : "";
        this.reportedAt = reportedAt != null ? reportedAt : LocalDate.now();
        this.publisher = publisher != null ? publisher : "";
        this.url = url != null ? url : "";
    }

}
