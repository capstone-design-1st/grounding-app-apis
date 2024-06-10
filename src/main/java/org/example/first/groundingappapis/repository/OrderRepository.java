package org.example.first.groundingappapis.repository;

import org.example.first.groundingappapis.dto.AccountDto;
import org.example.first.groundingappapis.entity.Order;
import org.example.first.groundingappapis.entity.Property;
import org.example.first.groundingappapis.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query("SELECT new org.example.first.groundingappapis.dto.AccountDto$ReadCompletedOrderResponse" +
            "(o.property.id, o.property.name, o.quantity, o.createdAt, o.type, o.price, p.type) " +
            "FROM Order o " +
            "JOIN o.property p " +
            "WHERE o.user = :user " +
            "AND o.createdAt BETWEEN :parsedStartDate AND :parsedEndDate " +
            "AND o.status = '체결 완료'")
    Page<AccountDto.ReadCompletedOrderResponse> findByUserAndCreatedAtBetweenAndCompleted(User user, LocalDateTime parsedStartDate, LocalDateTime parsedEndDate, Pageable pageable);


    @Query("SELECT new org.example.first.groundingappapis.dto.AccountDto$ReadCompletedOrderResponse" +
            "(o.property.id, o.property.name, o.quantity, o.createdAt, o.type, o.price, p.type) " +
            "FROM Order o " +
            "JOIN o.property p " +
            "WHERE o.user = :user " +
            "AND o.createdAt BETWEEN :parsedStartDate AND :parsedEndDate " +
            "AND o.status = '체결 완료' " +
            "AND o.type = :type")
    Page<AccountDto.ReadCompletedOrderResponse> findByUserAndCreatedAtBetweenAndTypeAndCompleted(User user, LocalDateTime parsedStartDate, LocalDateTime parsedEndDate, Pageable pageable, String type);

    @Query("SELECT o FROM Order o WHERE o.status = :status AND o.property = :property")
    List<Order> findByStatusAndProperty(String status, Property property);

    @Query(value = "SELECT SUM(o.price * o.quantity) FROM `orders` o WHERE o.status = :status AND o.user_id = :userId", nativeQuery = true)
    Long sumPriceByStatusAndUserId(@Param("status") String status, UUID userId);

    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END " +
            "FROM Order o " +
            "WHERE o.user = :user " +
            "AND o.property = :property " +
            "AND o.type = :type")
    Boolean existsByUserAndPropertyAndType(@Param("user") User user,
                                           @Param("property") Property property,
                                           @Param("type") String type);

    @Query("SELECT o FROM Order o WHERE o.user = :user AND o.property = :property AND o.price = :price AND o.type = :type AND o.status = :status")
    List<Optional<Order>> findByUserAndPropertyAndPriceAndTypeAndStatus(User user, Property property, int price, String type, String status);

    @Query("SELECT o FROM Order o WHERE o.user = :buyer AND o.property = :property AND o.type = :type AND o.price = :price AND o.status = :status")
    Order findByUserAndPropertyAndTypeAndPriceAndStatus(User buyer, Property property, String type, int price, String status);
}
