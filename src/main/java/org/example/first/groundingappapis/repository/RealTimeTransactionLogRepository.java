package org.example.first.groundingappapis.repository;

import org.example.first.groundingappapis.entity.RealTimeTransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RealTimeTransactionLogRepository extends JpaRepository<RealTimeTransactionLog, Long> {
}
