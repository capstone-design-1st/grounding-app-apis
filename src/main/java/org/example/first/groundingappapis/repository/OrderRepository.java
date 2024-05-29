package org.example.first.groundingappapis.repository;

import org.example.first.groundingappapis.entity.Order;
import org.example.first.groundingappapis.entity.Property;
import org.example.first.groundingappapis.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    Optional<Order> findByUserAndPropertyAndPriceAndQuantityAndType(User seller, Property property, int executedPrice, int executedQuantity, String status);
}
