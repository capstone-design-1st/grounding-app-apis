package org.example.first.groundingappapis.repository;

import org.example.first.groundingappapis.dto.RealTimeTransactionLogDto;
import org.example.first.groundingappapis.entity.Property;
import org.example.first.groundingappapis.entity.RealTimeTransactionLog;
import org.example.first.groundingappapis.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RealTimeTransactionLogRepository extends JpaRepository<RealTimeTransactionLog, UUID> {
    @Query("SELECT new org.example.first.groundingappapis.dto.RealTimeTransactionLogDto(" +
            "r.property.id, r.executedAt, r.quantity, r.executedPrice, r.fluctuationRate) " +
            "FROM RealTimeTransactionLog r " +
            "WHERE r.property IN :properties AND r.user = :user " +
            "ORDER BY r.executedAt DESC")
    List<RealTimeTransactionLogDto> findRecentTransactionLogsByUserAndProperties(@Param("user") User user, @Param("properties") List<Property> properties);

    @Query("SELECT new org.example.first.groundingappapis.dto.RealTimeTransactionLogDto(" +
            "r.property.id, r.executedAt, r.quantity, r.executedPrice, r.fluctuationRate) " +
            "FROM RealTimeTransactionLog r " +
            "WHERE r.property IN :properties " +
            "ORDER BY r.executedAt DESC")
    List<RealTimeTransactionLogDto> findRecentTransactionLogsByProperties(List<Property> properties);


//    @Query("SELECT new org.example.first.groundingappapis.dto.RealTimeTransactionLogDto(" +
//            "r.property.id, r.executedPrice) " +
//            "FROM RealTimeTransactionLog r " +
//            "WHERE r.property = :property " +
//            "ORDER BY r.executedAt DESC")

//UUID propertyId, LocalDateTime executedAt, Integer quantity, Integer executedPrice, Double fluctuationRate) {
    @Query("SELECT new org.example.first.groundingappapis.dto.RealTimeTransactionLogDto(" +
            "r.property.id, r.executedAt, r.quantity, r.executedPrice, r.fluctuationRate) " +
            "FROM RealTimeTransactionLog r " +
            "WHERE r.property = :property " +
            "ORDER BY r.executedAt DESC")
    Optional<RealTimeTransactionLogDto> findFirstByPropertyOrderByExecutedAtDesc(@Param("property") Property property);

    @Query("SELECT CASE COUNT(r) WHEN 0 THEN false ELSE true END " +
            "FROM RealTimeTransactionLog r " +
            "WHERE r.property.id = :propertyId")
    boolean existsByPropertyId(UUID propertyId);

    @Query("SELECT r " +
            "FROM RealTimeTransactionLog r " +
            "WHERE r.property.id = :propertyId " +
            "ORDER BY r.executedAt DESC")
    Optional<RealTimeTransactionLog> findFirstByPropertyIdOrderByExecutedAtDesc(UUID propertyId);

    @Query("SELECT CASE COUNT(r) WHEN 0 THEN false ELSE true END " +
            "FROM RealTimeTransactionLog r " +
            "WHERE r.property = :property AND r.user = :user")
    boolean existsByPropertyAndUser(Property property, User user);

    @Query("SELECT new org.example.first.groundingappapis.dto.RealTimeTransactionLogDto$ReadResponse" +
            "(r.property.id, r.executedAt, r.quantity, r.executedPrice, r.fluctuationRate) " +
            "FROM RealTimeTransactionLog r " +
            "WHERE r.property = :property " +
            "AND r.executedAt BETWEEN :startOfDay AND :endOfDay " +
            "ORDER BY r.executedAt DESC")
    Page<RealTimeTransactionLogDto.ReadResponse> readRealTimeTransactionLogsByPropertyAndExecutedAtToday(Property property, LocalDateTime startOfDay, LocalDateTime endOfDay, Pageable pageable);
}
