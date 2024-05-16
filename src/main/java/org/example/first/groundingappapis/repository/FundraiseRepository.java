package org.example.first.groundingappapis.repository;

import org.example.first.groundingappapis.entity.Fundraise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FundraiseRepository extends JpaRepository<Fundraise, Long> {
}
