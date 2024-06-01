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
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DayTransactionLogRepository extends JpaRepository<DayTransactionLog, UUID> {

    @Query("SELECT new org.example.first.groundingappapis.dto.DayTransactionLogDto(" +
            "d.property.id, d.date, d.fluctuationRate, d.openingPrice, d.closingPrice, d.maxPrice, d.minPrice) " +
            "FROM DayTransactionLog d " +
            "WHERE d.property IN :properties " +
            "ORDER BY d.date DESC")
    List<DayTransactionLogDto> findRecentDayTransactionLogsByProperties(List<Property> properties);

    @Query("SELECT d " +
            "FROM DayTransactionLog d " +
            "WHERE d.property = :property " +
            "ORDER BY d.date DESC")
    Optional<DayTransactionLog> findRecentDayTransactionLogByProperty(Property property);

    @Query("SELECT d " +
            "FROM DayTransactionLog d " +
            "WHERE d.property = :property " +
            "AND d.date = :date")
    Optional<DayTransactionLog> findRecentDayTransactionLogByPropertyAndToday(Property property, LocalDate date);

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
            "d.property.id, d.date, d.fluctuationRate, d.openingPrice, d.closingPrice, d.maxPrice, d.minPrice, d.volume) " +
            "FROM DayTransactionLog d " +
            "WHERE d.property.id = :propertyId " +
            "ORDER BY d.date DESC")
    Page<DayTransactionLogDto.ReadResponse> readDayTransactionLogsByPropertyId(UUID propertyId, Pageable pageable);
}
