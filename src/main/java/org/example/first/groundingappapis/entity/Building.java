package org.example.first.groundingappapis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "buildings")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Building {

    @Id
    @Column(name = "building_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID id;

    @PrePersist
    public void prePersist() {
        if (this.id == null)
            this.id = UUID.randomUUID();
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false, columnDefinition = "BINARY(16)", foreignKey = @ForeignKey(name = "fk_buildings_property"))
    private Property property;

    //사용면적
    @Column(name = "use_area", length = 20)
    private String useArea;

    //주용도
    @Column(name = "main_use", length = 20)
    private String mainUse;

    //연면적
    @Column(name = "total_floor_area")
    private Double totalFloorArea;

    //대지면적
    @Column(name = "land_area")
    private Double landArea;

    //건물규모
    @Column(name = "scale", length = 20)
    private String scale;

    //준공일
    @Column(name = "completion_date")
    private LocalDate completionDate;

    //공시지가
    @Column(name = "official_land_price", length = 20)
    private String officialLandPrice;

    //임차인
    @Column(name = "leaser", length = 20)
    private String leaser;

    //임대시작일
    @Column(name = "lease_start_date")
    private LocalDate leaseStartDate;

    //임대종료일
    @Column(name = "lease_end_date")
    private LocalDate leaseEndDate;

    //층수
    @Column(name = "floor_count")
    private String floorCount;

    @Builder
    public Building(String useArea, String mainUse, Double totalFloorArea, Double landArea, String scale, LocalDate completionDate, String officialLandPrice, String leaser, LocalDate leaseStartDate, LocalDate leaseEndDate, String floorCount) {
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
        this.floorCount = floorCount;
    }

    public void updateProperty(Property property) {
        this.property = property;
        property.setBuilding(this);
    }
}