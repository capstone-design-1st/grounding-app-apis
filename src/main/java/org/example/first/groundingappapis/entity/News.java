package org.example.first.groundingappapis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.first.groundingappapis.dto.NewsDto;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "news", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"title", "property_id"})
})
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class News {

    @Id
    @Column(name = "news_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID id;

    @PrePersist
    public void prePersist() {
        if (this.id == null)
            this.id = UUID.randomUUID();
    }

    @Column(name = "s3_url", length = 200)
    private String s3Url;

    @Column(name = "cloudfront_url", length = 200)
    private String cloudfrontUrl;

    @Column(name = "title", length = 50)
    private String title;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "url", length = 200)
    private String url;

    @Lob
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "reported_at")
    private LocalDate reportedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false, columnDefinition = "BINARY(16)", foreignKey = @ForeignKey(name = "fk_news_property"))
    private Property property;

    @Builder
    public News(String s3Url,
                String cloudfrontUrl,
                String title,
                String content,
                LocalDate reportedAt,
                String publisher,
                String url,
                Property property) {
        this.s3Url = s3Url;
        this.cloudfrontUrl = cloudfrontUrl;
        this.title = title;
        this.content = content;
        this.reportedAt = reportedAt;
        this.property = property;
        this.publisher = publisher;
        this.url = url;
    }

    public void updateProperty(Property property) {
        this.property = property;
        property.getNews().add(this);
    }

    public NewsDto toDto() {
        return NewsDto.builder()
                .s3Url(s3Url)
                .cloudfrontUrl(cloudfrontUrl)
                .title(title)
                .content(content)
                .reportedAt(reportedAt)
                .publisher(publisher)
                .url(url)
                .build();
    }
}

