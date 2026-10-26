package com.nuvora.nuvora.promotions.repository;

import com.nuvora.nuvora.promotions.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
}
