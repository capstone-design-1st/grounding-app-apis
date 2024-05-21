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
    @Column(name = "property_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID id;

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
    @BatchSize(size = 100)
    private Set<Like> likes = new LinkedHashSet<>();

    @OneToMany(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 100)
    private Set<RepresentationPhotoUrl> representationPhotoUrls = new LinkedHashSet<>();

    @OneToMany(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 100)
    private Set<News> news = new LinkedHashSet<>();

    @OneToMany(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 100)
    private Set<InvestmentPoint> investmentPoints = new LinkedHashSet<>();

    @OneToMany(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 100)
    private Set<Document> documents = new LinkedHashSet<>();

    //공시
    @OneToMany(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 100)
    private Set<Disclosure> disclosures = new LinkedHashSet<>();

    //호가
//    @OneToMany(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    @BatchSize(size = 100)
//    private Set<Quote> quotes = new LinkedHashSet<>();

    @PrePersist
    public void prePersist() {
        if (this.id == null)
            this.id = UUID.randomUUID();
        this.createdAt = (this.createdAt == null) ? LocalDateTime.now() : this.createdAt;
    }

    @Builder
    public Property(String name,
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
                    Set<Like> likes,
                    Set<RepresentationPhotoUrl> representationPhotoUrls,
                    Set<News> news,
                    Set<InvestmentPoint> investmentPoints,
                    Set<Document> documents,
                    Set<Disclosure> disclosures
                    //Set<Quote> quotes
    ) {
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
        this.investmentPoints = investmentPoints;
        this.documents = documents;
        this.disclosures = disclosures;
        //this.quotes = quotes;
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
                .id(this.id)
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

    public Set<Like> getLikes() {
        if(likes == null) {
            return new LinkedHashSet<>();
        }
        return likes;
    }

    public Set<RepresentationPhotoUrl> getRepresentationPhotoUrls() {
        if(representationPhotoUrls == null) {
            return new LinkedHashSet<>();
        }
        return representationPhotoUrls;
    }

    public Set<News> getNews() {
        if(news == null) {
            return new LinkedHashSet<>();
        }
        return news;
    }

    public Set<InvestmentPoint> getInvestmentPoints() {
        if(investmentPoints == null) {
            return new LinkedHashSet<>();
        }
        return investmentPoints;
    }

    public Set<Document> getDocuments() {
        if(documents == null) {
            return new LinkedHashSet<>();
        }
        return documents;
    }

    public Set<Disclosure> getDisclosures() {
        if(disclosures == null) {
            return new LinkedHashSet<>();
        }
        return disclosures;
    }

//    public Set<Quote> getQuotes() {
//        if(quotes == null) {
//            return new LinkedHashSet<>();
//        }
//        return quotes;
//    }
}
