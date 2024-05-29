package org.example.first.groundingappapis.repository;

import org.example.first.groundingappapis.entity.DepositLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DepositLogRepository extends JpaRepository<DepositLog, UUID> {
}
