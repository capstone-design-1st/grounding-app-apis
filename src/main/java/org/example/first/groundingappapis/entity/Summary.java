package org.example.first.groundingappapis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.first.groundingappapis.dto.SummaryDto;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "summaries")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Summary {

    @Id
    @Column(name = "summary_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID id;

    @Lob
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", unique = true, nullable = false, columnDefinition = "BINARY(16)", foreignKey = @ForeignKey(name = "fk_summaries_property"))
    private Property property;

    @PrePersist
    public void prePersist() {
        this.createdAt = (this.createdAt == null) ? LocalDateTime.now() : this.createdAt;
        if (this.id == null)
            this.id = UUID.randomUUID();
    }
    @Builder
    public Summary(String content, LocalDateTime createdAt) {
        this.content = content;
        this.createdAt = createdAt;
    }

    public SummaryDto toDto() {
        return SummaryDto.builder()
                .content(this.content)
                .build();
    }
}
