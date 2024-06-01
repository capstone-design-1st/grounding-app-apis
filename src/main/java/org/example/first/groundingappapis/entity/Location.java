package org.example.first.groundingappapis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.first.groundingappapis.dto.LocationDto;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "locations", indexes = {
        @Index(name = "idx_location_city", columnList = "city"),
        @Index(name = "idx_location_gu", columnList = "gu")
})
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    @Id
    @Column(name = "location_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID id;

    @PrePersist
    public void prePersist() {
        if (this.id == null)
            this.id = UUID.randomUUID();
    }

        @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false, columnDefinition = "BINARY(16)", foreignKey = @ForeignKey(name = "fk_location_property"))
    private Property property;

    @Column(name = "city", length = 10)
    private String city;

    @Column(name = "gu", length = 10)
    private String gu;

    @Column(name = "dong", length = 10)
    private String dong;

    @Column(name = "detail", length = 10)
    private String detail;

    @Builder
    public Location(Property property, String city, String gu, String dong, String detail) {
        this.property = property;
        this.city = city;
        this.gu = gu;
        this.dong = dong;
        this.detail = detail;
    }

    public void updateProperty(Property property) {
        this.property = property;
        property.setLocation(this);
    }

    public LocationDto toDto() {
        return LocationDto.builder()
                .city(city)
                .gu(gu)
                .dong(dong)
                .detail(detail)
                .build();
    }
}
