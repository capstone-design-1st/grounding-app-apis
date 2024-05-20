package org.example.first.groundingappapis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.first.groundingappapis.dto.DisclosureDto;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "disclosures")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Disclosure {
    @Id
    @Column(name = "disclosure_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID id;

    @PrePersist
    public void prePersist() {
        if (this.id == null)
            this.id = UUID.randomUUID();
    }

    @Column(name = "title")
    private String title;

    @Lob
    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false, columnDefinition = "BINARY(16)", foreignKey = @ForeignKey(name = "fk_disclosures_property"))
    private Property property;

    @Column(name = "reported_at")
    private LocalDate reportedAt;

    @Builder
    public Disclosure(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void updateProperty(Property property) {
        this.property = property;
        property.getDisclosures().add(this);
    }

    public DisclosureDto toDto() {
        return DisclosureDto.builder()
                .id(id)
                .title(title)
                .content(content)
                .reportedAt(reportedAt)
                .build();
    }
}