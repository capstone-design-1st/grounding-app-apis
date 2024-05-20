package org.example.first.groundingappapis.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.first.groundingappapis.dto.PropertyDto;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.*;

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

    @Column(name = "property_name", length = 50)
    private String name;

    @Column(name = "oneline", length = 50)
    private String oneline;

    @Column(name = "present_price")
    private Long presentPrice;

    @Column(name = "view_count")
    private Long viewCount;

    @Column(name = "like_count")
    private Long likeCount;

    @Column(name = "volume_count")
    private Long volumeCount;

    @Column(name = "type", nullable = false)
    private String type;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    //조각모집
    @OneToOne(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Fundraise fundraise;

    @OneToOne(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private ThumbnailUrl thumbnailUrl;

    @OneToOne(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Land land;

    @OneToOne(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Building building;

    @OneToOne(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Location location;

    @OneToMany(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Like> likes = new LinkedHashSet<>();

    @OneToMany(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RepresentationPhotoUrl> representationPhotoUrls = new LinkedHashSet<>();
    //private List<RepresentationPhotoUrl> representationPhotoUrls = new ArrayList<>();


    @OneToMany(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<News> news = new ArrayList<>();

    @OneToMany(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvestmentPoint> investmentPoints = new ArrayList<>();

    @OneToMany(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Document> documents = new ArrayList<>();

    //공시
    @OneToMany(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Disclosure> disclosures = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.uuid = (this.uuid == null) ? UUID.randomUUID() : this.uuid;
        this.createdAt = (this.createdAt == null) ? LocalDateTime.now() : this.createdAt;
    }

    @Builder
    public Property(UUID uuid,
                    String name,
                    String oneline,
                    Long presentPrice,
                    Long viewCount,
                    Long likeCount,
                    String type,
                    LocalDateTime createdAt,
                    LocalDateTime updatedAt,
                    ThumbnailUrl thumbnailUrl,
                    Land land,
                    Building building,
                    Fundraise fundraise,
                    List<Like> likes,
                    List<RepresentationPhotoUrl> representationPhotoUrls,
                    List<News> news) {
        this.uuid = uuid;
        this.name = name;
        this.oneline = oneline;
        this.presentPrice = presentPrice;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.type = type;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.thumbnailUrl = thumbnailUrl;
        this.land = land;
        this.building = building;
        this.fundraise = fundraise;
        this.likes = likes;
        this.representationPhotoUrls = representationPhotoUrls;
        this.news = news;
    }

    public void setLand(Land land) {
        this.land = land;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setFundraise(Fundraise fundraise) {
        this.fundraise = fundraise;
    }

    public void setThumbnailUrl(ThumbnailUrl thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public void increaseViewCount(){
        this.viewCount++;
    }

    public void increaseLikeCount(){
        this.likeCount++;
    }

    public void decreaseLikeCount(){
        this.likeCount--;
    }

    public void increaseVolumeCount(){
        this.volumeCount++;
    }

    public PropertyDto toDto() {
        return PropertyDto.builder()
                .uuid(this.uuid)
                .name(this.name)
                .oneline(this.oneline)
                .presentPrice(this.presentPrice)
                .viewCount(this.viewCount)
                .likeCount(this.likeCount)
                .volumeCount(this.volumeCount)
                .type(this.type)
                .build();
    }

    public ThumbnailUrl getThumbnailUrl() {
        if(thumbnailUrl == null) {
            return new ThumbnailUrl();
        }
        return thumbnailUrl;
    }

    public Land getLand() {
        if(land == null) {
            return new Land();
        }
        return land;
    }

    public Building getBuilding() {
        if(building == null) {
            return new Building();
        }
        return building;
    }

    public Location getLocation() {
        if (location == null) {
            return new Location();
        }
        return location;
    }

    public Fundraise getFundraise() {
        if(fundraise == null) {
            return new Fundraise();
        }
        return fundraise;
    }

    public List<Like> getLikes() {
        if(likes == null) {
            return new ArrayList<>();
        }
        return likes;
    }

    public List<RepresentationPhotoUrl> getRepresentationPhotoUrls() {
        if(representationPhotoUrls == null) {
            return new ArrayList<>();
        }
        return representationPhotoUrls;
    }

    public List<News> getNews() {
        if(news == null) {
            return new ArrayList<>();
        }
        return news;
    }

    public List<InvestmentPoint> getInvestmentPoints() {
        if(investmentPoints == null) {
            return new ArrayList<>();
        }
        return investmentPoints;
    }

    public List<Document> getDocuments() {
        if(documents == null) {
            return new ArrayList<>();
        }
        return documents;
    }

    public List<Disclosure> getDisclosures() {
        if(disclosures == null) {
            return new ArrayList<>();
        }
        return disclosures;
    }

}
