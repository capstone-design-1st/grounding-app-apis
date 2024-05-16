package org.example.first.groundingappapis.repository;

import org.example.first.groundingappapis.entity.DepositLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositLogRepository extends JpaRepository<DepositLog, Long> {
}
