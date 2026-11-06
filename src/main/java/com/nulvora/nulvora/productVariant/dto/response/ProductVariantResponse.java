package com.nulvora.nulvora.productVariant.dto.response;



import com.nulvora.nulvora.productImg.dto.response.ProductImgResponse;

import java.math.BigDecimal;
import java.util.List;

public record ProductVariantResponse(
        Long id,
        String name,
        BigDecimal price,
        Integer stockQuantity,
        boolean isDefault,
        boolean active,

        List<ProductImgResponse> productImgs,
        List<ProductVariantAttributesResponse> attributes

) {
}
