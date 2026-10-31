package com.nulvora.nulvora.productVariant.repository;

import com.nulvora.nulvora.productVariant.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {


    // retorna o menor valor dentre os preçoes das variantes do produto
    @Query("""
    SELECT MIN(v.price)
    FROM ProductVariant v
    WHERE v.product.id = :productId
""")
    BigDecimal findMinPriceByProductId(Long productId);
}
