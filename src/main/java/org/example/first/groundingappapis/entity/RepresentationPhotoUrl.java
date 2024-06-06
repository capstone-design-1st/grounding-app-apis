package org.example.first.groundingappapis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.first.groundingappapis.dto.RepresentationPhotoUrlDto;

import java.util.UUID;

@Entity
@Table(name = "representation_photo_urls", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"cloudfront_url", "property_id"})
})
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RepresentationPhotoUrl {
    @Id
    @Column(name = "representation_photo_url_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false, columnDefinition = "BINARY(16)", foreignKey = @ForeignKey(name = "fk_representation_photo_urls_property"))
    private Property property;

    @Column(name = "s3Url", length = 255)
    private String s3Url;

    @Column(name = "cloudfront_url", length = 255, nullable = false, unique = true)
    private String cloudfrontUrl;

    @Builder
    public RepresentationPhotoUrl(String s3Url, String cloudfrontUrl) {
        this.s3Url = s3Url;
        this.cloudfrontUrl = cloudfrontUrl;
    }

    public void updateProperty(Property property) {
        this.property = property;
        property.getRepresentationPhotoUrls().add(this);
    }

    @PrePersist
    public void prePersist() {
        if (this.id == null)
            this.id = UUID.randomUUID();
    }

    public RepresentationPhotoUrlDto toDto() {
        return RepresentationPhotoUrlDto.builder()
                .s3Url(this.s3Url)
                .cloudfrontUrl(this.cloudfrontUrl)
                .build();
    }
}
