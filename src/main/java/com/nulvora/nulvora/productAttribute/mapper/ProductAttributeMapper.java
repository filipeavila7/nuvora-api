package com.nulvora.nulvora.productAttribute.mapper;


import com.nulvora.nulvora.product.entity.Product;
import com.nulvora.nulvora.productAttribute.entity.ProductAttribute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductAttributeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", source = "product")
    @Mapping(target = "name", source = "name")
    ProductAttribute createProductAttribute(Product product, String name);


}
