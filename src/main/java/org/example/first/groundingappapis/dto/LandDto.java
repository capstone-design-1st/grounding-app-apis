package org.example.first.groundingappapis.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

//@TODO : 땅은 다르게
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder(builderClassName = "LandBuilder", builderMethodName = "landBuilder")
public class LandDto implements ListingInformation{
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
}
