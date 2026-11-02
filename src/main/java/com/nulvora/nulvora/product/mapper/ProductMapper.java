package com.nulvora.nulvora.product.mapper;

import com.nulvora.nulvora.product.dto.request.ProductRequest;
import com.nulvora.nulvora.product.dto.response.ProductAttributeResponse;
import com.nulvora.nulvora.product.dto.response.ProductPostResponse;
import com.nulvora.nulvora.product.entity.Product;
import com.nulvora.nulvora.productAttribute.entity.ProductAttribute;
import com.nulvora.nulvora.productAttributeOption.entity.ProductAttributeOption;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toEntity(ProductRequest request);

    @Mapping(target = "attributes", source = "productAttributes")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "isActive", source = "active")
    ProductPostResponse toProductPostResponse(Product product);

    ProductAttributeResponse toProductAttributeResponse(ProductAttribute productAttribute);

    default String toOptionResponse(ProductAttributeOption option) {
        return option.getValue();
    }
}
