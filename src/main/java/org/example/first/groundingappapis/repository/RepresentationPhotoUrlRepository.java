package org.example.first.groundingappapis.repository;

import org.example.first.groundingappapis.entity.RepresentationPhotoUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepresentationPhotoUrlRepository extends JpaRepository<RepresentationPhotoUrl, Long> {
}
