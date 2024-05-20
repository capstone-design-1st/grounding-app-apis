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
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ThumbnailUrlDto {
    private String s3Url;
    private String cloudfrontUrl;

    @Builder
    public ThumbnailUrlDto(String s3Url,
                           String cloudfrontUrl) {
        this.s3Url = s3Url != null ? s3Url : "";
        this.cloudfrontUrl = cloudfrontUrl != null ? cloudfrontUrl : "";
    }
}
