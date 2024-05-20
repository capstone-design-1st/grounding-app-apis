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
public class BuildingDto implements PropertyDetailDto {
    private String useArea;
    private String mainUse;
    private Double totalFloorArea;
    private Double landArea;
    private String scale;
    private LocalDate completionDate;
    private String officialLandPrice;
    private String leaser;
    private LocalDate leaseStartDate;
    private LocalDate leaseEndDate;
    private String floorCount;

    @Builder(builderClassName = "BuildingBuilder", builderMethodName = "buildingBuilder")
    public BuildingDto(String useArea,
                       String mainUse,
                       Double totalFloorArea,
                       Double landArea,
                       String scale,
                       LocalDate completionDate,
                       String officialLandPrice,
                       String leaser,
                       LocalDate leaseStartDate,
                       LocalDate leaseEndDate,
                       String floorCount) {
        this.useArea = useArea != null ? useArea : "";
        this.mainUse = mainUse != null ? mainUse : "";
        this.totalFloorArea = totalFloorArea != null ? totalFloorArea : 0.0;
        this.landArea = landArea != null ? landArea : 0.0;
        this.scale = scale != null ? scale : "";
        this.completionDate = completionDate != null ? completionDate : LocalDate.now();
        this.officialLandPrice = officialLandPrice != null ? officialLandPrice : "";
        this.leaser = leaser != null ? leaser : "";
        this.leaseStartDate = leaseStartDate != null ? leaseStartDate : LocalDate.now();
        this.leaseEndDate = leaseEndDate != null ? leaseEndDate : LocalDate.now();
        this.floorCount = floorCount != null ? floorCount : "";
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
