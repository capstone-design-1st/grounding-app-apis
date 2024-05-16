package org.example.first.groundingappapis.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "properties")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "property_id")
    private Long id;

    @Column(name = "property_uuid", columnDefinition = "BINARY(16)", unique = true)
    private UUID uuid;

    @Column(name = "name", length = 50)
    private String name;

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

    @OneToOne(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private ThumbnailUrl thumbnailUrl;

    @OneToOne(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Land land;

    @OneToOne(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Building building;

    //조각모집
    @OneToOne(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Fundraise fundraise;

    @OneToMany(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RepresentationPhotoUrl> representationPhotoUrls = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.uuid = (this.uuid == null) ? UUID.randomUUID() : this.uuid;
        this.createdAt = (this.createdAt == null) ? LocalDateTime.now() : this.createdAt;
    }

    public String getType(){
        if(this.land != null){
            return "land";
        }else if(this.building != null){
            return "building";
        }else{
            return null;
        }
    }

    @Builder
    public Property(UUID uuid, String name, Long viewCount, Long likeCount, LocalDateTime createdAt, LocalDateTime updatedAt, ThumbnailUrl thumbnailUrl, Land land, Building building, Fundraise fundraise, List<Like> likes, List<RepresentationPhotoUrl> representationPhotoUrls) {
        this.uuid = uuid;
        this.name = name;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.thumbnailUrl = thumbnailUrl;
        this.land = land;
        this.building = building;
        this.fundraise = fundraise;
        this.likes = likes;
        this.representationPhotoUrls = representationPhotoUrls;
    }

    public void setLand(Land land) {
        this.land = land;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

}
