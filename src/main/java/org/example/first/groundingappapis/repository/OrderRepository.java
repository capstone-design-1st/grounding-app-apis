package org.example.first.groundingappapis.repository;

import org.example.first.groundingappapis.dto.AccountDto;
import org.example.first.groundingappapis.entity.Order;
import org.example.first.groundingappapis.entity.Property;
import org.example.first.groundingappapis.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    Optional<Order> findByUserAndPropertyAndPriceAndQuantityAndType(User seller, Property property, int executedPrice, int executedQuantity, String status);

    @Query("SELECT new org.example.first.groundingappapis.dto.AccountDto$ReadCompletedOrderResponse" +
            "(o.property.id, o.property.name, o.quantity, o.createdAt, o.type, o.price) " +
            "FROM Order o " +
            "JOIN o.property p " +
            "WHERE o.user = :user " +
            "AND o.createdAt BETWEEN :parsedStartDate AND :parsedEndDate " +
            "AND o.status = '체결 완료'")
    Page<AccountDto.ReadCompletedOrderResponse> findByUserAndCreatedAtBetweenAndCompleted(User user, LocalDateTime parsedStartDate, LocalDateTime parsedEndDate, Pageable pageable);

    @Query("SELECT new org.example.first.groundingappapis.dto.AccountDto$ReadCompletedOrderResponse" +
            "(o.property.id, o.property.name, o.quantity, o.createdAt, o.type, o.price) " +
            "FROM Order o " +
            "JOIN o.property p " +
            "WHERE o.user = :user " +
            "AND o.createdAt BETWEEN :parsedStartDate AND :parsedEndDate " +
            "AND o.status = '체결 완료' " +
            "AND o.type = :type")
    Page<AccountDto.ReadCompletedOrderResponse> findByUserAndCreatedAtBetweenAndTypeAndCompleted(User user, LocalDateTime parsedStartDate, LocalDateTime parsedEndDate, Pageable pageable, String type);
}
