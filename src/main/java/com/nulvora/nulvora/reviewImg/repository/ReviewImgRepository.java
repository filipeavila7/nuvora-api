package com.nulvora.nulvora.reviewImg.repository;

import com.nulvora.nulvora.reviewImg.entity.ReviewImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewImgRepository extends JpaRepository<ReviewImg, Long> {
}
