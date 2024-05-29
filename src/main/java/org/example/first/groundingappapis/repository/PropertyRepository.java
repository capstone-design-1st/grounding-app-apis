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
}

