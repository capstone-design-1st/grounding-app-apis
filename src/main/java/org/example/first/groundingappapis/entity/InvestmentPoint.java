package org.example.first.groundingappapis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.first.groundingappapis.dto.InvestmentPointDto;

import java.util.UUID;

@Entity
@Table(name = "investment_points")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentPoint {
    @Id
    @Column(name = "investment_point_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID id;

    @PrePersist
    public void prePersist() {
        if (this.id == null)
            this.id = UUID.randomUUID();
    }

    @Column(name = "title", length = 50)
    private String title;

    @Lob
    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false, columnDefinition = "BINARY(16)", foreignKey = @ForeignKey(name = "fk_investment_points_property"))
    private Property property;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "summary_id", nullable = false, columnDefinition = "BINARY(16)", foreignKey = @ForeignKey(name = "fk_investment_points_summary"))
    private Summary summary;

    @Builder
    public InvestmentPoint(String title, String content, Property property, Summary summary) {
        this.title = title;
        this.content = content;
        this.property = property;
        this.summary = summary;
    }

    public InvestmentPointDto toDto() {
        return InvestmentPointDto.builder()
                .title(this.title)
                .content(this.content)
                .build();
    }
}
