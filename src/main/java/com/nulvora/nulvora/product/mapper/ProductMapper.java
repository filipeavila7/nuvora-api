package com.nulvora.nulvora.product.mapper;

import com.nulvora.nulvora.product.dto.request.ProductRequest;
import com.nulvora.nulvora.product.dto.response.ProductPostResponse;
import com.nulvora.nulvora.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toEntity(ProductRequest request);

    @Mapping(target = "categoryName", source = "category.name")
    ProductPostResponse toProductPostResponse(Product product);


}
