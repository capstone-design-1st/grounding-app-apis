package org.example.first.groundingappapis.repository;

import org.example.first.groundingappapis.entity.Like;
import org.example.first.groundingappapis.entity.Property;
import org.example.first.groundingappapis.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LikeRepository extends JpaRepository<Like, UUID> {

    @Query("SELECT l FROM Like l WHERE l.property = :property AND l.user = :user")
    Optional<Like> findByPropertyAndUser(Property property, User user);

    /*@Query("SELECT new com.blackshoe.moongklheremobileapi.dto.PostDto$PostListReadResponse(" +
            "p.id, p.user.id, p.skinUrl.cloudfrontUrl, p.storyUrl.cloudfrontUrl) " +
            "FROM Post p " +
            "INNER JOIN Like l ON p.id = l.post.id " +
            "WHERE l.user = :user AND p.isPublic = true " +
            "ORDER BY l.createdAt DESC")
        @Builder
        public ReadBasicInfoResponse(UUID id,
                                     String name,
                                     Long presentPrice,
                                     Long priceDifference,
                                     Double fluctuationRate,
                                     Long viewCount,
                                     Long likeCount,
                                     Long volumeCount,
                                     String type) {
            this.id = id;
            this.name = name;
            this.presentPrice = presentPrice;
            this.priceDifference = priceDifference;
            this.fluctuationRate = fluctuationRate;
            this.viewCount = viewCount;
            this.likeCount = likeCount;
            this.volumeCount = volumeCount;
            this.type = type;
        }
     */

    @Query("SELECT l.property FROM Like l WHERE l.user = :user")
    Page<Property> findAllLikedPropertyByUser(User user, Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Like l " +
            "WHERE l.property = :property AND l.user = :user")
    boolean existsByPropertyAndUser(Property property, User user);
}
