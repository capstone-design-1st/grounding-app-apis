package org.example.first.groundingappapis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.first.groundingappapis.dto.LandDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "lands")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Land {
    @Id
    @Column(name = "land_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID id;

    @PrePersist
    public void prePersist() {
        if (this.id == null)
            this.id = UUID.randomUUID();
    }
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", unique = true, nullable = false,columnDefinition = "BINARY(16)", foreignKey = @ForeignKey(name = "fk_lands_property"))
    private Property property;
    //면적
    @Column(name = "area", length = 20)
    private String area;

    //지목
    @Column(name = "land_use", length = 30)
    private String landUse;

    //기타사항
    @Column(name = "etc", length = 50)
    private String etc;

    //추천용도
    @Column(name = "recommend_use", length = 30)
    private String recommendUse;

    //주차 가능 여부
    @Column(name = "parking", columnDefinition = "TINYINT")
    private boolean parking;

    //가장 가까운 기차역
    @Column(name = "nearest_station", length = 50)
    private String nearestStation;

    @Builder
    public Land(Property property, String area, String landUse, String etc, String recommendUse, boolean parking, String nearestStation) {
        this.property = property;
        this.area = area;
        this.landUse = landUse;
        this.etc = etc;
        this.recommendUse = recommendUse;
        this.parking = parking;
        this.nearestStation = nearestStation;
    }

    public void updateProperty(Property property) {
        this.property = property;
        property.setLand(this);
    }

    //toDto
    public LandDto toDto() {
        return LandDto.landBuilder()
                .area(area)
                .landUse(landUse)
                .etc(etc)
                .recommendUse(recommendUse)
                .parking(parking)
                .nearestStation(nearestStation)
                .build();
    }

}
