package org.example.first.groundingappapis.repository;

import org.example.first.groundingappapis.dto.DepositLogDto;
import org.example.first.groundingappapis.entity.Account;
import org.example.first.groundingappapis.entity.DepositLog;
import org.example.first.groundingappapis.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface DepositLogRepository extends JpaRepository<DepositLog, UUID> {
    /*
        @Data
    @Builder
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ReadResponse{
        private Long amount;
        private String type;
        private LocalDateTime createdAt;

        @Builder
        public ReadResponse(Long amount, String type, LocalDateTime createdAt) {
            this.amount = amount != null ? amount : 0L;
            this.type = type != null ? type : "";
            this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        }
    }

     */
    @Query("SELECT new org.example.first.groundingappapis.dto.DepositLogDto$ReadResponse(d.amount, d.type, d.createdAt) " +
            "FROM DepositLog d " +
            "WHERE d.account = :account " +
            "AND d.createdAt BETWEEN :startDate AND :endDate " +
            "ORDER BY d.createdAt DESC")
    Page<DepositLogDto.ReadResponse> findByAccountAndCreatedAtBetween(Account account, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    @Query("SELECT new org.example.first.groundingappapis.dto.DepositLogDto$ReadResponse(d.amount, d.type, d.createdAt) " +
            "FROM DepositLog d " +
            "WHERE d.account = :account AND d.type = :type " +
            "AND d.createdAt BETWEEN :startDate AND :endDate " +
            "ORDER BY d.createdAt DESC")
    Page<DepositLogDto.ReadResponse> findByAccountAndCreatedAtBetweenAndType(Account account, LocalDateTime startDate, LocalDateTime endDate, String type, Pageable pageable);
}
