package org.example.first.groundingappapis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "likes")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Like {

    @Id
    @Column(name = "like_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "BINARY(16)", foreignKey = @ForeignKey(name = "fk_likes_user"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", columnDefinition = "BINARY(16)", nullable = false)
    private Property property;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public Like(User user, Property property) {
        this.user = user;
        this.property = property;
    }
    @PrePersist
    public void prePersist() {
        if (this.id == null)
            this.id = UUID.randomUUID();

        this.createdAt = (this.createdAt == null) ? LocalDateTime.now() : this.createdAt;
    }

    public void updateUser(User user) {
        this.user = user;
        user.getLikes().add(this);
    }

    public void updateProperty(Property property) {
        this.property = property;
        property.getLikes().add(this);
    }
}
