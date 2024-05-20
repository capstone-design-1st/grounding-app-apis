package org.example.first.groundingappapis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "disclosures")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Disclosure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "disclosure_id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Lob
    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false, foreignKey = @ForeignKey(name = "fk_disclosures_property"))
    private Property property;

    @Column(name = "reported_at")
    private LocalDate reportedAt;

    @Builder
    public Disclosure(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void updateProperty(Property property) {
        this.property = property;
        property.getDisclosures().add(this);
    }
}