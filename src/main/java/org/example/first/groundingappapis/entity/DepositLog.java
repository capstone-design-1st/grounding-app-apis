package org.example.first.groundingappapis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.first.groundingappapis.dto.DepositLogDto;

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
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false, columnDefinition = "BINARY(16)", foreignKey = @ForeignKey(name = "fk_deposit_logs_account"))
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "BINARY(16)", foreignKey = @ForeignKey(name = "fk_deposit_logs_user"))
    private User user;

    //매수, 매도
    @Column(name = "type", length = 10)
    private String type;

    @Column(name = "amount")
    private Long amount;

    private void updateAccount(Account account) {
        this.account = account;
        //account.getDepositLogs().add(this);
    }

    //입금
    public void setLogToDeposit(Long amount) {
        this.type = "입금";
        this.amount = amount;
    }

    //출금
    public void setLogToWithdraw(Long amount) {
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
    public DepositLog(Account account, User user, String type, Long amount) {
        this.account = account;
        this.user = user;
        this.type = type;
        this.amount = amount;
    }


}
