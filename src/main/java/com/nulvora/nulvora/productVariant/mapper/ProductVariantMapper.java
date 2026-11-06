package com.nulvora.nulvora.productVariant.mapper;

import com.nulvora.nulvora.product.entity.Product;
import com.nulvora.nulvora.productImg.dto.response.ProductImgResponse;
import com.nulvora.nulvora.productImg.entity.ProductImg;
import com.nulvora.nulvora.productVariant.dto.request.ProductVariantRequest;
import com.nulvora.nulvora.productVariant.dto.response.ProductVariantAttributesResponse;
import com.nulvora.nulvora.productVariant.dto.response.ProductVariantResponse;
import com.nulvora.nulvora.productVariant.entity.ProductVariant;
import com.nulvora.nulvora.variantAttribute.entity.VariantAttribute;
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


    ProductVariant toEntity(ProductVariantRequest request);


    // o campo attributer do dto se refere a lista de variantAttributes
    @Mapping(target = "attributes", source = "variantAttributes")
    @Mapping(target = "productImgs", source = "productImgs")
    ProductVariantResponse toResponse(ProductVariant productVariant);

    // o mapper tenta encontrar algum metodo que recebe "variantAttributes" e acha esse
    @Mapping(
            target = "attributeName", // o campo attributeName vai receber o nome do atributo (navega entre as chaves)
            source = "option.productAttribute.name"
    )

    @Mapping(
            target = "optionName", // campo optionName recebe o valor
            source = "option.value"
    )

    ProductVariantAttributesResponse toAttributeResponse(
            VariantAttribute variantAttribute
    );

    @Mapping(target = "primary", source = "primary")
    ProductImgResponse toImgResponse(ProductImg productImg);
}
