package org.example.first.groundingappapis.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.first.groundingappapis.dto.ThumbnailUrlDto;

import java.util.UUID;

@Entity
@Table(name = "thumbnail_urls", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"property_id", "cloudfront_url"})
})
@Getter
@NoArgsConstructor
public class ThumbnailUrl {

    @Id
    @Column(name = "thumbnail_url_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID id;


    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "property_id", unique = true, nullable = false, columnDefinition = "BINARY(16)", foreignKey = @ForeignKey(name = "fk_thumbnail_urls_property"))
    private Property property;

    @Column(name = "s3_url", length = 300)
    private String s3Url;

    @Column(name = "cloudfront_url", length = 300)
    private String cloudfrontUrl;

    @Builder
    public ThumbnailUrl(String s3Url, String cloudfrontUrl) {
        this.s3Url = s3Url;
        this.cloudfrontUrl = cloudfrontUrl;
    }

    public void updateProperty(Property property) {
        this.property = property;
        property.setThumbnailUrl(this);
    }

    @PrePersist
    public void prePersist() {
        if (this.id == null)
            this.id = UUID.randomUUID();
    }

    public ThumbnailUrlDto toDto() {
        return ThumbnailUrlDto.builder()
                .s3Url(s3Url)
                .cloudfrontUrl(cloudfrontUrl)
                .build();
    }
}
