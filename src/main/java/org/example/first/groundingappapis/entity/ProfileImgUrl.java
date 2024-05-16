package org.example.first.groundingappapis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "profile_img_urls")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileImgUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_img_url_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_profile_img_urls_user"))
    private User user;

    @Column(name = "s3_url", length = 100)
    private String s3Url;

    @Column(name = "cloudfront_url", length = 100)
    private String cloudfrontUrl;

    @Builder
    public ProfileImgUrl(User user, String s3Url, String cloudfrontUrl) {
        this.user = user;
        this.s3Url = s3Url;
        this.cloudfrontUrl = cloudfrontUrl;
    }

    public void updateUser(User user) {
        this.user = user;
        user.setProfileImgUrl(this);
    }
}
