package org.example.first.groundingappapis.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "thumbnail_urls")
@Getter
@NoArgsConstructor
public class ThumbnailUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "thumbnail_url_id")
    private Long thumbnailUrlId;

    @Id
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @Column(name = "s3_url", length = 100)
    private String s3Url;

    @Column(name = "cloudfront_url", length = 100)
    private String cloudfrontUrl;

    @Builder
    public ThumbnailUrl(Property property, String s3Url, String cloudfrontUrl) {
        this.s3Url = s3Url;
        this.cloudfrontUrl = cloudfrontUrl;
    }

    public void updateProperty(Property property) {
        this.property = property;
        property.setThumbnailUrl(this);
    }
}
