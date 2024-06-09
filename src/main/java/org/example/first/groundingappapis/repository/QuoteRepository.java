package org.example.first.groundingappapis.repository;

import org.example.first.groundingappapis.dto.QuoteDto;
import org.example.first.groundingappapis.entity.Property;
import org.example.first.groundingappapis.entity.Quote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, UUID> {

    @Query("SELECT CASE WHEN COUNT(q) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Quote q " +
            "JOIN Order o ON q.property = o.property " +
            "WHERE q.property = :property AND q.price <= :price " +
            "AND o.type = '매도' AND o.status = '체결대기중'")
    boolean existsByPropertyAndPriceLessThanEqualWithOrderStatus(
            @Param("property") Property property,
            @Param("price") int price
    );

    //@Query("SELECT q FROM Quote q WHERE q.property = :property AND q.price <= :price AND q.type = : type ORDER BY q.price ASC, q.createdAt ASC") native query, limit 1
//
//    @Query(value = "SELECT * FROM quotes q WHERE q.property_id = :propertyId " +
//            "AND q.price <= :price " +
//            "AND q.type = :type " +
//            "ORDER BY q.price ASC, q.created_at ASC LIMIT 1", nativeQuery = true)

    @Query(value = "SELECT * FROM quotes q WHERE q.property_id = :propertyId " +
            "AND q.price <= :price " +
            "AND q.type = :type " +
            "ORDER BY q.price ASC, q.created_at ASC LIMIT 1", nativeQuery = true)
    Quote findFirstByPropertyAndPriceLessThanEqualAndTypeOrderByPriceAsc(@Param("propertyId") UUID propertyId, int price, String type);

    @Query(value = "SELECT * FROM quotes q WHERE q.property_id = :propertyId " +
            "AND q.price >= :price " +
            "AND q.type = :type " +
            "ORDER BY q.price DESC, q.created_at ASC LIMIT 1", nativeQuery = true)
    Quote findFirstByPropertyAndPriceGreaterThanEqualOrderByPriceDesc(@Param("propertyId") UUID propertyId, int price, String type);

    @Query("SELECT CASE WHEN COUNT(q) > 0 THEN TRUE ELSE FALSE END FROM Quote q WHERE q.property = :property AND q.price >= :price")
    boolean existsByPropertyAndPriceGreaterThanEqual(Property property, int price);

    @Query("SELECT new org.example.first.groundingappapis.dto.QuoteDto$ReadResponse" +
            "(q.price, SUM(q.quantity), q.type)" +
            " FROM Quote q " +
            "WHERE q.property.id = :propertyId AND q.price >= :basePrice " +
            "GROUP BY q.price, q.type " +
            "ORDER BY q.price DESC")
    Page<QuoteDto.ReadResponse> findByPropertyIdAndPriceGreaterThanEqualOrderByPriceDesc(@Param("propertyId") UUID propertyId, int basePrice, Pageable pageable);

    @Query("SELECT new org.example.first.groundingappapis.dto.QuoteDto$ReadResponse" +
            "(q.price, SUM(q.quantity), q.type)" +
            " FROM Quote q " +
            "WHERE q.property.id = :propertyId AND q.price < :basePrice " +
            "GROUP BY q.price, q.type " +
            "ORDER BY q.price ASC")
    Page<QuoteDto.ReadResponse> findByPropertyIdAndPriceLessOrderByPriceAsc(@Param("propertyId") UUID propertyId, int basePrice, Pageable pageable);

}
