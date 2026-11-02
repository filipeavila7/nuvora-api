package com.nulvora.nulvora.productAttribute.service;


import com.nulvora.nulvora.product.entity.Product;
import com.nulvora.nulvora.productAttribute.entity.ProductAttribute;
import com.nulvora.nulvora.productAttribute.mapper.ProductAttributeMapper;
import com.nulvora.nulvora.productAttribute.repository.ProductAttributeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductAttributeService {
    private final ProductAttributeRepository productAttributeRepository;
    private final ProductAttributeMapper productAttributeMapper;

    // criar atributos
    public ProductAttribute createProductAttribute(Product product, String name){
        return productAttributeRepository.
                save(productAttributeMapper.createProductAttribute(product, name));

    }

}
