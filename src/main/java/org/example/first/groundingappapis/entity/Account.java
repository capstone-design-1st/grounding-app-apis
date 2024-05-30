package org.example.first.groundingappapis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.first.groundingappapis.dto.AccountDto;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "accounts")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @Column(name = "account_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID id;

    @PrePersist
    public void prePersist() {
        if (this.id == null)
            this.id = UUID.randomUUID();
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "BINARY(16)", foreignKey = @ForeignKey(name = "fk_accounts_user"))
    private User user;

    @Column(name = "deposit", columnDefinition = "BIGINT DEFAULT 0")
    private Long deposit;

    @Column(name = "total_earning_rate", columnDefinition = "DOUBLE DEFAULT 0.0")
    private Double totalEarningRate;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Quote> quotes = new LinkedHashSet<>();

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DepositLog> depositLogs = new LinkedHashSet<>();

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Inventory> inventories = new LinkedHashSet<>();

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
    public void plusDeposit(Long amount) {
        this.deposit += amount;
    }

    public void minusDeposit(Long amount) {
        this.deposit -= amount;
    }
}
