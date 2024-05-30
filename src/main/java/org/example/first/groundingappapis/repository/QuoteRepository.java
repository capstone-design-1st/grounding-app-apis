package org.example.first.groundingappapis.repository;

import org.example.first.groundingappapis.dto.QuoteDto;
import org.example.first.groundingappapis.entity.Property;
import org.example.first.groundingappapis.entity.Quote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("SELECT new org.example.first.groundingappapis.dto.QuoteDto$ReadResponse" +
            "(q.price, q.quantity, q.createdAt)" +
            " FROM Quote q " +
            "WHERE q.property.id = :propertyId AND q.price >= :basePrice " +
            "ORDER BY q.price DESC, q.createdAt ASC")
    Page<QuoteDto.ReadResponse> findByPropertyIdAndPriceGreaterThanEqualOrderByPriceDesc(UUID propertyId, int basePrice, Pageable pageable);

    @Query("SELECT new org.example.first.groundingappapis.dto.QuoteDto$ReadResponse" +
            "(q.price, q.quantity, q.createdAt)" +
            " FROM Quote q " +
            "WHERE q.property.id = :propertyId AND q.price < :basePrice " +
            "ORDER BY q.price ASC, q.createdAt ASC")
    Page<QuoteDto.ReadResponse> findByPropertyIdAndPriceLessOrderByPriceAsc(UUID propertyId, int basePrice, Pageable pageable);
}
