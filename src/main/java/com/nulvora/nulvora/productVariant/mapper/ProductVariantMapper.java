package com.nulvora.nulvora.productVariant.mapper;

import com.nulvora.nulvora.product.entity.Product;
import com.nulvora.nulvora.productVariant.entity.ProductVariant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductVariantMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sku", ignore = true)
    @Mapping(target = "product", source = "product")
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "productImgs", ignore = true)
    @Mapping(target = "variantAttributes", ignore = true)
    @Mapping(target = "default", constant = "true")
    @Mapping(target = "active", constant = "true")
    ProductVariant toDefaultVariant(Product product);
}
