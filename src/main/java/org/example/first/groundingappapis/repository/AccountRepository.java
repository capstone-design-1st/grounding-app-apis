package org.example.first.groundingappapis.repository;

import org.example.first.groundingappapis.entity.Account;
import org.example.first.groundingappapis.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByUser(User user);
}
