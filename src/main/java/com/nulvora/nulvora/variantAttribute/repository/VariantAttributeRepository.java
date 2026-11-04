package com.nulvora.nulvora.variantAttribute.repository;

import com.nulvora.nulvora.variantAttribute.entity.VariantAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VariantAttributeRepository extends JpaRepository<VariantAttribute, Long> {

    List<VariantAttribute> findAllByOption_ProductAttribute_Product_Id(Long productId);
}
