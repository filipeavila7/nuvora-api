package com.nulvora.nulvora.product.mapper;

import com.nulvora.nulvora.product.dto.request.ProductRequest;
import com.nulvora.nulvora.product.dto.request.ProductUpdateRequest;
import com.nulvora.nulvora.product.dto.response.ProductAttributeResponse;
import com.nulvora.nulvora.product.dto.response.ProductResponse;
import com.nulvora.nulvora.product.entity.Product;
import com.nulvora.nulvora.productAttribute.entity.ProductAttribute;
import com.nulvora.nulvora.productAttributeOption.entity.ProductAttributeOption;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toEntity(ProductRequest request);

    @Mapping(target = "attributes", source = "productAttributes")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "isActive", source = "active")
    ProductResponse toProductResponse(Product product);

    ProductAttributeResponse toProductAttributeResponse(ProductAttribute productAttribute);

    default String toOptionResponse(ProductAttributeOption option) {
        return option.getValue();
    }


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productVariants", ignore = true)
    @Mapping(target = "productAttributes", ignore = true)
    @Mapping(target = "category", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(ProductUpdateRequest request, @MappingTarget Product product);
}
