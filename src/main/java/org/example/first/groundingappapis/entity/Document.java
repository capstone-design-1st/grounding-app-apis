package org.example.first.groundingappapis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.first.groundingappapis.dto.DocumentDto;

import java.util.UUID;

@Entity
@Table(name = "documents")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Document {
    @Id
    @Column(name = "document_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID id;

    @PrePersist
    public void prePersist() {
        if (this.id == null)
            this.id = UUID.randomUUID();
    }

    @Column(name = "title")
    private String title;

    @Column(name = "s3Url", length = 255)
    private String s3Url;

    @Column(name = "cloudfront_url", length = 255)
    private String cloudfrontUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false, columnDefinition = "BINARY(16)", foreignKey = @ForeignKey(name = "fk_documents_property"))
    private Property property;

    @Builder
    public Document(String s3Url, String cloudfrontUrl) {
        this.s3Url = s3Url;
        this.cloudfrontUrl = cloudfrontUrl;
    }

    public void updateProperty(Property property) {
        this.property = property;
        property.getDocuments().add(this);
    }

    public DocumentDto toDto() {
        return DocumentDto.builder()
                .id(id)
                .title(title)
                .s3Url(s3Url)
                .cloudfrontUrl(cloudfrontUrl)
                .build();
    }
}