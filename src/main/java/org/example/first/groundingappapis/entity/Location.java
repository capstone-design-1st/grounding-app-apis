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

@Entity
@Table(name = "locations")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false, foreignKey = @ForeignKey(name = "fk_location_property"))
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
