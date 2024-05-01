package org.example.first.groundingappapis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "thumbnail_urls")
@Getter
@NoArgsConstructor
public class ThumbnailUrl {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_img_url_id", nullable = false)
    private ProfileImgUrl profileImgUrl;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @Column(name = "s3_url", length = 100)
    private String s3Url;

    @Column(name = "cloudfront_url", length = 100)
    private String cloudfrontUrl;

    @Builder
    public ThumbnailUrl(ProfileImgUrl profileImgUrl, Property property, String s3Url, String cloudfrontUrl) {
        this.profileImgUrl = profileImgUrl;
        this.property = property;
        this.s3Url = s3Url;
        this.cloudfrontUrl = cloudfrontUrl;
    }
}
