package com.nuvora.nuvora.reviewImg.repository;

import com.nuvora.nuvora.reviewImg.entity.ReviewImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewImgRepository extends JpaRepository<ReviewImg, Long> {
}
