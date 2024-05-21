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

import java.util.List;

@Repository
public interface RealTimeTransactionLogRepository extends JpaRepository<RealTimeTransactionLog, Long> {
    @Query("SELECT new org.example.first.groundingappapis.dto.RealTimeTransactionLogDto(" +
            "r.property.id, r.executedAt, r.quantity, r.executedPrice, r.fluctuationRate) " +
            "FROM RealTimeTransactionLog r " +
            "WHERE r.property IN :properties AND r.user = :user " +
            "ORDER BY r.executedAt DESC")
    List<RealTimeTransactionLogDto> findRecentTransactionLogsByUserAndProperties(@Param("user") User user, @Param("properties") List<Property> properties);

}
