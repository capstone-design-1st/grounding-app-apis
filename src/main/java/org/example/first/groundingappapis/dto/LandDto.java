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
public class LandDto implements PropertyDetailDto{
    private String area;
    private String landUse;
    private String etc;
    private String recommendUse;
    private boolean parking;
    private String nearestStation;

    @Builder(builderClassName = "LandBuilder", builderMethodName = "landBuilder")
    public LandDto(String area, String landUse, String etc, String recommendUse, boolean parking, String nearestStation) {
        this.area = area != null ? area : "";
        this.landUse = landUse != null ? landUse : "";
        this.etc = etc != null ? etc : "";
        this.recommendUse = recommendUse != null ? recommendUse : "";
        this.parking = parking != false;
        this.nearestStation = nearestStation != null ? nearestStation : "";
    }

    @Data
    @Builder
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class GetResponse{}
    @Data
    @Builder
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ReadResponse{}
}
