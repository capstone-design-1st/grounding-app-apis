package org.example.first.groundingappapis.repository;

import org.example.first.groundingappapis.entity.Land;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LandRepository extends JpaRepository<Land, Long> {
}
