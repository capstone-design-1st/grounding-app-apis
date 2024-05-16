package org.example.first.groundingappapis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "representation_photo_urls")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RepresentationPhotoUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "representation_photo_url_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false, foreignKey = @ForeignKey(name = "fk_buildings_property"))
    private Property property;

    @Column(name = "s3Url", length = 255)
    private String s3Url;

    @Column(name = "cloudfrontUrl", length = 255)
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
}