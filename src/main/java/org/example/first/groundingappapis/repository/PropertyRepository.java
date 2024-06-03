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

//    @Query(value = "SELECT BIN_TO_UUID(p.property_id) AS propertyId, p.type AS type, l.city AS city, l.gu AS gu, p.property_name AS propertyName, p.oneline AS oneline, " +
//            "(SELECT rt.fluctuation_rate FROM real_time_transaction_logs rt WHERE rt.property_id = p.property_id ORDER BY rt.executed_at DESC LIMIT 1) AS fluctuationRate " +
//            "FROM properties p " +
//            "LEFT JOIN locations l ON p.property_id = l.property_id " +
//            "WHERE p.property_name LIKE %:keyword% OR l.city LIKE %:keyword% OR l.gu LIKE %:keyword%",
//            countQuery = "SELECT COUNT(*) " +
//                    "FROM properties p " +
//                    "LEFT JOIN locations l ON p.property_id = l.property_id " +
//                    "WHERE p.property_name LIKE %:keyword% OR l.city LIKE %:keyword% OR l.gu LIKE %:keyword%",
//            nativeQuery = true)
@Query(value = "SELECT BIN_TO_UUID(p.property_id) AS propertyId, p.type AS type, l.city AS city, l.gu AS gu, p.property_name AS propertyName, p.oneline AS oneline, " +
        "CASE " +
        "  WHEN rt.fluctuation_rate IS NOT NULL THEN rt.fluctuation_rate " +
        "  ELSE dt.fluctuation_rate " +
        "END AS fluctuationRate " +
        "FROM properties p " +
        "LEFT JOIN locations l ON p.property_id = l.property_id " +
        "LEFT JOIN (SELECT property_id, fluctuation_rate FROM real_time_transaction_logs ORDER BY executed_at DESC LIMIT 1) rt ON p.property_id = rt.property_id " +
        "LEFT JOIN (SELECT property_id, fluctuation_rate FROM day_transaction_logs ORDER BY date DESC LIMIT 1) dt ON p.property_id = dt.property_id " +
        "WHERE p.property_name LIKE %:keyword% OR l.city LIKE %:keyword% OR l.gu LIKE %:keyword%",
        countQuery = "SELECT COUNT(*) " +
                "FROM properties p " +
                "LEFT JOIN locations l ON p.property_id = l.property_id " +
                "WHERE p.property_name LIKE %:keyword% OR l.city LIKE %:keyword% OR l.gu LIKE %:keyword%",
        nativeQuery = true)
    Page<Object[]> searchProperties(@Param("keyword") String keyword, Pageable pageable);

/*
search all property,
 */
    @Query(value = "SELECT BIN_TO_UUID(p.property_id) AS propertyId, p.type AS type, l.city AS city, l.gu AS gu, p.property_name AS propertyName, p.oneline AS oneline, " +
            "CASE " +
            "  WHEN rt.fluctuation_rate IS NOT NULL THEN rt.fluctuation_rate " +
            "  ELSE dt.fluctuation_rate " +
            "END AS fluctuationRate " +
            "FROM properties p " +
            "LEFT JOIN locations l ON p.property_id = l.property_id " +
            "LEFT JOIN (SELECT property_id, fluctuation_rate FROM real_time_transaction_logs ORDER BY executed_at DESC LIMIT 1) rt ON p.property_id = rt.property_id " +
            "LEFT JOIN (SELECT property_id, fluctuation_rate FROM day_transaction_logs ORDER BY date DESC LIMIT 1) dt ON p.property_id = dt.property_id",
            countQuery = "SELECT COUNT(*) " +
                    "FROM properties p " +
                    "LEFT JOIN locations l ON p.property_id = l.property_id",
            nativeQuery = true)
    Page<Object[]> searchProperties(Pageable pageable);
}
