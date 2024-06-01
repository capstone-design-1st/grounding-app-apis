package org.example.first.groundingappapis.repository;

import org.example.first.groundingappapis.dto.PropertyDto;
import org.example.first.groundingappapis.entity.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PropertyRepository extends JpaRepository<Property, UUID>{

//    @Query("SELECT new org.example.first.groundingappapis.dto.PropertyDto$ReadResponse " +
//            "(p.uuid, p.name, p.price, p.pieceCount, p.piecePrice, p.viewCount, p.likeCount, p.createdAt, p.updatedAt) " +
//            "FROM Property p ORDER BY p.createdAt DESC")
//    Page<PropertyDto.ReadResponse> readAllByOrderByCreatedAtDesc(Pageable pageable);
//
//    @Query("SELECT new org.example.first.groundingappapis.dto.PropertyDto$ReadResponse " +
//            "(p.uuid, p.name, p.price, p.pieceCount, p.piecePrice, p.viewCount, p.likeCount, p.createdAt, p.updatedAt) " +
//            "FROM Property p WHERE p.uuid = :propertyId")
//    PropertyDto.ReadResponse readByUuid(@Param(value = "propertyId")UUID propertyId);

    @Query("SELECT p FROM Property p " +
            "LEFT JOIN FETCH p.fundraise " +
            "LEFT JOIN FETCH p.land " +
            "LEFT JOIN FETCH p.building " +
            "LEFT JOIN FETCH p.location " +
            "LEFT JOIN FETCH p.thumbnailUrl " +
            "LEFT JOIN FETCH p.representationPhotoUrls " +
            "LEFT JOIN FETCH p.news " +
            "LEFT JOIN FETCH p.investmentPoints " +
            "LEFT JOIN FETCH p.documents " +
            "WHERE p.id = :id")
    Optional<Property> getDetailPropertyById(UUID id);

    @Query("SELECT p FROM Property p WHERE p.id = :id")
    Optional<Property> findById(UUID id);

//    @Query(value = "SELECT p.property_id, p.type, l.city, l.gu, p.property_name, p.oneline, " +
//            "(SELECT rt.fluctuation_rate FROM real_time_transaction_logs rt WHERE rt.property_id = p.property_id ORDER BY rt.executed_at DESC LIMIT 1) " +
//            "FROM properties p " +
//            "LEFT JOIN locations l ON p.id = l.property_id " +
//            "WHERE p.property_name LIKE %:keyword% OR l.city LIKE %:keyword% OR l.gu LIKE %:keyword%",
//            countQuery = "SELECT COUNT(*) " +
//                    "FROM properties p " +
//                    "LEFT JOIN locations l ON p.id = l.property_id " +
//                    "WHERE p.property_name LIKE %:keyword% OR l.city LIKE %:keyword% OR l.gu LIKE %:keyword%",
//            nativeQuery = true)
    /*
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
@Table(name = "properties", indexes = {
        @Index(name = "idx_property_name", columnList = "property_name")
})
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

    //썸네일
    @OneToOne(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private ThumbnailUrl thumbnailUrl;

    @OneToOne(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Land land;

    @OneToOne(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Building building;

    //위치정보
    @OneToOne(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Location location;

    @OneToMany(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 100)
    private Set<Like> likes = new LinkedHashSet<>();

    //대표사진들
    @OneToMany(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 100)
    private Set<RepresentationPhotoUrl> representationPhotoUrls = new LinkedHashSet<>();

    //뉴스
    @OneToMany(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 100)
    private Set<News> news = new LinkedHashSet<>();

    //투자 포인트
    @OneToMany(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 100)
    private Set<InvestmentPoint> investmentPoints = new LinkedHashSet<>();

    //참고문서
    @OneToMany(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 100)
    private Set<Document> documents = new LinkedHashSet<>();

    //공시
    @OneToMany(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 100)
    private Set<Disclosure> disclosures = new LinkedHashSet<>();

    //호가
    @OneToMany(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 100)
    private Set<Quote> quotes = new LinkedHashSet<>();

    @OneToMany(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 100)
    private Set<RealTimeTransactionLog> realTimeTransactionLogs = new LinkedHashSet<>();

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

    public Set<Quote> getQuotes() {
        if(quotes == null) {
            return new LinkedHashSet<>();
        }
        return quotes;
    }

    public Set<RealTimeTransactionLog> getRealTimeTransactionLogs() {
        if(realTimeTransactionLogs == null) {
            return new LinkedHashSet<>();
        }
        return realTimeTransactionLogs;
    }
}

     */
    @Query(value = "SELECT BIN_TO_UUID(p.property_id) AS propertyId, p.type AS type, l.city AS city, l.gu AS gu, p.property_name AS propertyName, p.oneline AS oneline, " +
            "(SELECT rt.fluctuation_rate FROM real_time_transaction_logs rt WHERE rt.property_id = p.property_id ORDER BY rt.executed_at DESC LIMIT 1) AS fluctuationRate " +
            "FROM properties p " +
            "LEFT JOIN locations l ON p.property_id = l.property_id " +
            "WHERE p.property_name LIKE %:keyword% OR l.city LIKE %:keyword% OR l.gu LIKE %:keyword%",
            countQuery = "SELECT COUNT(*) " +
                    "FROM properties p " +
                    "LEFT JOIN locations l ON p.property_id = l.property_id " +
                    "WHERE p.property_name LIKE %:keyword% OR l.city LIKE %:keyword% OR l.gu LIKE %:keyword%",
            nativeQuery = true)
    Page<Object[]> searchProperties(@Param("keyword") String keyword, Pageable pageable);
}
