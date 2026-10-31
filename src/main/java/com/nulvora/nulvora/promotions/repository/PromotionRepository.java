package com.nulvora.nulvora.promotions.repository;

import com.nulvora.nulvora.promotions.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
}
