package com.nuvora.nuvora.productKeywords.repository;

import com.nuvora.nuvora.productKeywords.entity.ProductKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductKeywordRepository extends JpaRepository<ProductKeyword, Long> {
}
