package com.nulvora.nulvora.productAttribute.repository;

import com.nulvora.nulvora.productAttribute.entity.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, Long> {
}
