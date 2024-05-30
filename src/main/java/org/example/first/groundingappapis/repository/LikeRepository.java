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

    @Query("SELECT l.property FROM Like l WHERE l.user = :user")
    Page<Property> findAllLikedPropertyByUser(User user, Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Like l " +
            "WHERE l.property = :property AND l.user = :user")
    boolean existsByPropertyAndUser(Property property, User user);
}
