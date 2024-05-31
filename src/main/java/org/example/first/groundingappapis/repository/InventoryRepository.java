package org.example.first.groundingappapis.repository;

import org.example.first.groundingappapis.entity.Account;
import org.example.first.groundingappapis.entity.Inventory;
import org.example.first.groundingappapis.entity.Property;
import org.example.first.groundingappapis.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, UUID>{

    @Query("SELECT i FROM Inventory i WHERE i.account = :buyerAccount AND i.property = :property")
    boolean existsByAccountAndProperty(Account buyerAccount, Property property);

    Optional<Inventory> findByAccountAndProperty(Account buyerAccount, Property property);


    @Query("SELECT i FROM Inventory i WHERE i.account = :account")
    List<Inventory> findByAccount(Account account);
}
