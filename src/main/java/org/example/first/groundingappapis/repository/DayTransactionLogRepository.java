package org.example.first.groundingappapis.repository;

import org.example.first.groundingappapis.dto.DayTransactionLogDto;
import org.example.first.groundingappapis.dto.RealTimeTransactionLogDto;
import org.example.first.groundingappapis.entity.DayTransactionLog;
import org.example.first.groundingappapis.entity.Property;
import org.example.first.groundingappapis.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.UUID;

@Repository
public interface DayTransactionLogRepository extends JpaRepository<DayTransactionLog, UUID> {

    @Query(value = "SELECT d.* FROM day_transaction_logs d " +
            "INNER JOIN (" +
            "    SELECT property_id, MAX(date) as max_date " +
            "    FROM day_transaction_logs " +
            "    WHERE property_id IN :propertyIds " +
            "    GROUP BY property_id" +
            ") sub ON d.property_id = sub.property_id AND d.date = sub.max_date",
            nativeQuery = true)
    List<DayTransactionLog> findRecentDayTransactionLogsByPropertyIds(@Param("propertyIds") List<UUID> propertyIds);

    @Query(value = "SELECT * FROM day_transaction_logs d " +
            "WHERE d.property_id = :propertyId " +
            "ORDER BY d.date DESC " +
            "LIMIT 1", nativeQuery = true)
    Optional<DayTransactionLog> findRecentDayTransactionLogByProperty(UUID propertyId);

    @Query(value = "SELECT * FROM day_transaction_logs d " +
            "WHERE d.property_id = :propertyId " +
            "AND d.date = :date " +
            "ORDER BY d.date DESC " +
            "LIMIT 1", nativeQuery = true)
    Optional<DayTransactionLog> findRecentDayTransactionLogByPropertyAndToday(@Param("propertyId") UUID propertyId, @Param("date") LocalDate date);

    @Query("SELECT d " +
            "FROM DayTransactionLog d " +
            "WHERE d.property = :property " +
            "AND d.date = :yesterday " +
            "ORDER BY d.date DESC")
    Optional<DayTransactionLog> findLastDayTransactionLogByProperty(Property property, LocalDate yesterday);

    /*
            private UUID propertyId;
        private LocalDate date;
        private Double fluctuationRate;
        private Integer openingPrice;
        private Integer closingPrice;
        private Integer maxPrice;
        private Integer minPrice;
        private Long volume;
     */
    @Query("SELECT new org.example.first.groundingappapis.dto.DayTransactionLogDto$ReadResponse(" +
            "d.property.id, d.date, d.fluctuationRate, d.openingPrice, d.closingPrice, d.maxPrice, d.minPrice, d.volumeCount) " +
            "FROM DayTransactionLog d " +
            "WHERE d.property.id = :propertyId " +
            "ORDER BY d.date DESC")
    Page<DayTransactionLogDto.ReadResponse> readDayTransactionLogsByPropertyId(UUID propertyId, Pageable pageable);

    @Query(value = "SELECT * FROM day_transaction_logs d " +
            "WHERE d.property_id = :propertyId " +
            "AND d.date = :localDate " +
            "ORDER BY d.date DESC", nativeQuery = true)
    Optional<DayTransactionLog> findByPropertyIdAndDate(UUID propertyId, LocalDate localDate);
}
