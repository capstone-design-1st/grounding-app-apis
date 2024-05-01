package org.example.first.groundingappapis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    private Long locationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @Column(name = "city", length = 10)
    private String city;

    @Column(name = "gu", length = 10)
    private String gu;

    @Column(name = "dong", length = 10)
    private String dong;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public Location(Property property, String city, String gu, String dong, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.property = property;
        this.city = city;
        this.gu = gu;
        this.dong = dong;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
