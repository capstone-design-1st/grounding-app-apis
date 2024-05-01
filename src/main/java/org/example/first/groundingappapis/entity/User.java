package org.example.first.groundingappapis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_uuid", columnDefinition = "BINARY(16)")
    private UUID userUuid;

    @Column(name = "email", nullable = false, length = 30)
    private String email;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "phone_number", length = 30)
    private String phoneNumber;

    @Column(name = "nickname", length = 10)
    private String nickname;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.userUuid = (this.userUuid == null) ? UUID.randomUUID() : this.userUuid;
    }

    @Builder
    public User(UUID userUuid, String email, String password, String phoneNumber, String nickname, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userUuid = userUuid;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.nickname = nickname;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
