package org.example.first.groundingappapis.repository;

import org.example.first.groundingappapis.dto.AccountDto;
import org.example.first.groundingappapis.dto.OrderDto;
import org.example.first.groundingappapis.entity.Account;
import org.example.first.groundingappapis.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByUser(User user);
    /*
        @Builder
        public ReadPresentStatusResponse(UUID userId,
                                          Long totalBuyingPrice,
                                          Long fundraisingPrice,
                                          Long deposit,
                                          Double averageEarningRate,
                                          Integer evaluationPrice,
                                          Integer evaluationEarning) {
            this.userId = userId;
            this.totalBuyingPrice = totalBuyingPrice != null ? totalBuyingPrice : 0L;
            this.fundraisingPrice = fundraisingPrice != null ? fundraisingPrice : 0L;
            this.deposit = deposit != null ? deposit : 0L;
            this.averageEarningRate = averageEarningRate != null ? averageEarningRate : 0.0;
            this.evaluationPrice = evaluationPrice != null ? evaluationPrice : 0;
            this.evaluationEarning = evaluationEarning != null ? evaluationEarning : 0;
        }
     */
    //native query, not JPQL, totalBuyingPrice = inventory.quantity * inventory.price


    AccountDto.ReadPresentStatusResponse readPresentationStatusByUserId(UUID userId);

    @Query(value = "SELECT CASE WHEN SUM(i.quantity) = 0 THEN 0 ELSE SUM(i.quantity * i.earnings_rate) / SUM(i.quantity) END AS average_earning_rate " +
            "FROM inventories i " +
            "WHERE i.account_id = :id", nativeQuery = true)
    Double getAverageEarningRateByAccount(UUID id);

}
