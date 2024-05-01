package org.example.first.groundingappapis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private Long accountId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "account_uuid", columnDefinition = "BINARY(16)")
    private UUID accountUuid;

    @Column(name = "deposit")
    private Long deposit;

    @Builder
    public Account(User user, UUID accountUuid, Long deposit) {
        this.user = user;
        this.accountUuid = accountUuid;
        this.deposit = deposit;
    }
}
