package org.example.first.groundingappapis.repository;

import org.example.first.groundingappapis.entity.DayTransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayTransactionLogRepository extends JpaRepository<DayTransactionLog, Long> {
}
