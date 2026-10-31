package com.nulvora.nulvora.productKeywords.repository;

import com.nulvora.nulvora.productKeywords.entity.ProductKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductKeywordRepository extends JpaRepository<ProductKeyword, Long> {
}
