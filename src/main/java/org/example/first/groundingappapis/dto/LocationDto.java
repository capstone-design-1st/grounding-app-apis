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
public class LocationDto {
    private String city;
    private String gu;
    private String dong;
    private String detail;

    @Builder
    public LocationDto(String city,
                       String gu,
                       String dong,
                       String detail) {
        this.city = city != null ? city : "";
        this.gu = gu != null ? gu : "";
        this.dong = dong != null ? dong : "";
        this.detail = detail != null ? detail : "";
    }
}
