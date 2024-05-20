package org.example.first.groundingappapis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.first.groundingappapis.dto.InvestmentPointDto;

@Entity
@Table(name = "investment_points")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "investment_point_id")
    private Long id;

    @Column(name = "title", length = 50)
    private String title;

    @Lob
    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "summary_id", nullable = false)
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
