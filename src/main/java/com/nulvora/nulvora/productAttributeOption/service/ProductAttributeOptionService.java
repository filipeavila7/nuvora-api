package com.nulvora.nulvora.productAttributeOption.service;


import com.nulvora.nulvora.productAttribute.entity.ProductAttribute;
import com.nulvora.nulvora.productAttributeOption.entity.ProductAttributeOption;
import com.nulvora.nulvora.productAttributeOption.mapper.ProductAttributeOptionMapper;
import com.nulvora.nulvora.productAttributeOption.repository.ProductAttributeOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductAttributeOptionService {
    private final ProductAttributeOptionRepository productAttributeOptionRepository;
    private final ProductAttributeOptionMapper productAttributeOptionMapper;


    public ProductAttributeOption createAttributeOption(ProductAttribute productAttribute, String value){
        return productAttributeOptionRepository
                .save(productAttributeOptionMapper.createProductAttributeOption(productAttribute, value));


    }


}
