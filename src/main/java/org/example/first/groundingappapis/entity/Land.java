package org.example.first.groundingappapis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "lands")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Land {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @Column(name = "land_uuid", columnDefinition = "BINARY(16)")
    private UUID landUuid;

    @Column(name = "use_area", length = 20)
    private String useArea;

    @Column(name = "main_use", length = 20)
    private String mainUse;

    @Column(name = "total_floor_area")
    private Double totalFloorArea;

    @Column(name = "land_area")
    private Double landArea;

    @Column(name = "scale", length = 20)
    private String scale;

    @Column(name = "completion_date")
    private LocalDate completionDate;

    @Column(name = "official_land_price", length = 20)
    private String officialLandPrice;

    @Column(name = "leaser", length = 20)
    private String leaser;

    @Column(name = "lease_start_date")
    private LocalDate leaseStartDate;

    @Column(name = "lease_end_date")
    private LocalDate leaseEndDate;

    @PrePersist
    public void prePersist() {
        this.landUuid = (this.landUuid == null) ? UUID.randomUUID() : this.landUuid;
    }

    @Builder
    public Land(Building building, Property property, UUID landUuid, String useArea, String mainUse, Double totalFloorArea, Double landArea, String scale, LocalDate completionDate, String officialLandPrice, String leaser, LocalDate leaseStartDate, LocalDate leaseEndDate) {
        this.building = building;
        this.property = property;
        this.landUuid = landUuid;
        this.useArea = useArea;
        this.mainUse = mainUse;
        this.totalFloorArea = totalFloorArea;
        this.landArea = landArea;
        this.scale = scale;
        this.completionDate = completionDate;
        this.officialLandPrice = officialLandPrice;
        this.leaser = leaser;
        this.leaseStartDate = leaseStartDate;
        this.leaseEndDate = leaseEndDate;
    }
}
