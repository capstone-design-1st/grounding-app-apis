package org.example.first.groundingappapis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.first.groundingappapis.dto.AccountDto;

import java.util.UUID;

@Entity
@Table(name = "accounts")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_accounts_user"))
    private User user;

    @Column(name = "deposit", columnDefinition = "BIGINT DEFAULT 0")
    private Long deposit;

    @Column(name = "total_earning_rate", columnDefinition = "DOUBLE DEFAULT 0.0")
    private Double totalEarningRate;

    @Builder
    public Account(User user, Long deposit, Double totalEarningRate) {
        this.user = user;
        this.deposit = deposit;
        this.totalEarningRate = totalEarningRate;
    }

    public AccountDto toDto() {
        return AccountDto.builder()
                .deposit(deposit)
                .totalEarningRate(totalEarningRate)
                .build();
    }
}
