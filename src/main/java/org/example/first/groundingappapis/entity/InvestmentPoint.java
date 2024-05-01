package org.example.first.groundingappapis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "investment_points")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "investment_point_id")
    private Long investmentPointId;

    @Column(name = "s3_url", length = 100)
    private String s3Url;

    @Column(name = "cloudfront_url", length = 100)
    private String cloudfrontUrl;

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
    public InvestmentPoint(String s3Url, String cloudfrontUrl, String title, String content, Property property, Summary summary) {
        this.s3Url = s3Url;
        this.cloudfrontUrl = cloudfrontUrl;
        this.title = title;
        this.content = content;
        this.property = property;
        this.summary = summary;
    }
}
