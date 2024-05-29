package org.example.first.groundingappapis.repository;

import org.example.first.groundingappapis.entity.Property;
import org.example.first.groundingappapis.entity.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, UUID> {

    @Query("SELECT CASE WHEN COUNT(q) > 0 THEN TRUE ELSE FALSE END FROM Quote q WHERE q.property = :property AND q.price <= :price")
    boolean existsByPropertyAndPriceLessThanEqual(Property property, int price);

    @Query("SELECT q FROM Quote q WHERE q.property = :property AND q.price <= :price ORDER BY q.price ASC, q.createdAt ASC")
    Optional<Quote> findFirstByPropertyAndPriceLessThanEqualOrderByPriceAsc(Property property, int price);

    @Query("SELECT q FROM Quote q WHERE q.property = :property AND q.price >= :price ORDER BY q.price DESC, q.createdAt ASC")
    Optional<Quote> findFirstByPropertyAndPriceGreaterThanEqualOrderByPriceDesc(Property property, int price);

    @Query("SELECT CASE WHEN COUNT(q) > 0 THEN TRUE ELSE FALSE END FROM Quote q WHERE q.property = :property AND q.price >= :price")
    boolean existsByPropertyAndPriceGreaterThanEqual(Property property, int price);
}
