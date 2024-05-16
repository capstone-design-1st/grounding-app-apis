package org.example.first.groundingappapis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "deposit_logs")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DepositLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deposit_log_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false, foreignKey = @ForeignKey(name = "fk_deposit_logs_account"))
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //매수, 매도
    @Column(name = "type", length = 10)
    private String type;

    private void updateAccount(Account account) {
        this.account = account;
        //account.getDepositLogs().add(this);
    }

    @Builder
    public DepositLog(Account account, User user, String type) {
        this.account = account;
        this.user = user;
        this.type = type;
    }
}
