package org.example.first.groundingappapis.repository;

import org.example.first.groundingappapis.entity.ThumbnailUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThumbnailUrlRepository extends JpaRepository<ThumbnailUrl, Long> {
}
