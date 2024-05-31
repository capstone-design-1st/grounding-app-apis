package org.example.first.groundingappapis.repository;

import org.example.first.groundingappapis.dto.DayTransactionLogDto;
import org.example.first.groundingappapis.dto.RealTimeTransactionLogDto;
import org.example.first.groundingappapis.entity.DayTransactionLog;
import org.example.first.groundingappapis.entity.Property;
import org.example.first.groundingappapis.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
}
