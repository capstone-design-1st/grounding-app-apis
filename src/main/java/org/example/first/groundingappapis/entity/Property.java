package org.example.first.groundingappapis.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "properties")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Property {

    @Id
    //AUto increment
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "property_id")
    private Long propertyId;

    @Column(name = "property_uuid", columnDefinition = "BINARY(16)")
    private UUID propertyUuid;

    @Column(name = "name", length = 10)
    private String name;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "nickname", length = 10)
    private String nickname;

    @Column(name = "view_count")
    private Long viewCount;

    @Column(name = "like_count")
    private Long likeCount;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ThumbnailUrl thumbnailUrl;

    @OneToOne(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Land land;

    @OneToOne(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Building building;

    @PrePersist
    public void prePersist() {
        this.propertyUuid = (this.propertyUuid == null) ? UUID.randomUUID() : this.propertyUuid;
    }

    @Builder
    public Property(UUID propertyUuid, String name, String password, String phoneNumber, String nickname, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.propertyUuid = propertyUuid;
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.nickname = nickname;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void setLand(Land land) {
        this.land = land;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }
}
