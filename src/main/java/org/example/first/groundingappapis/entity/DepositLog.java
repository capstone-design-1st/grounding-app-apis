package org.example.first.groundingappapis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.first.groundingappapis.dto.DepositLogDto;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "deposit_logs")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DepositLog {

    @Id
    @Column(name = "deposit_log_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID id;

    @PrePersist
    public void prePersist() {
        if (this.id == null)
            this.id = UUID.randomUUID();
        this.createdAt = (this.createdAt == null) ? LocalDateTime.now() : this.createdAt;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false, columnDefinition = "BINARY(16)", foreignKey = @ForeignKey(name = "fk_deposit_logs_account"))
    private Account account;

    //매수, 매도
    @Column(name = "type", length = 10)
    private String type;

    @Column(name = "amount")
    private Integer amount;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public void updateAccount(Account account) {
        this.account = account;
        account.getDepositLogs().add(this);
    }

    //입금
    public void setLogOfDeposit(Integer amount) {
        this.type = "입금";
        this.amount = amount;
    }

    //출금
    public void setLogOfWithdraw(Integer amount) {
        this.type = "출금";
        this.amount = amount;
    }

    public DepositLogDto toDto() {
        return DepositLogDto.builder()
                .type(type)
                .amount(amount)
                .build();
    }

    @Builder
    public DepositLog(Account account, String type, Integer amount, LocalDateTime createdAt) {
        this.account = account;
        this.type = type;
        this.amount = amount;
        this.createdAt = createdAt;
    }

}
