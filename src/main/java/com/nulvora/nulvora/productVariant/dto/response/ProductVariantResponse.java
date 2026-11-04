package com.nulvora.nulvora.productVariant.dto.response;



import java.math.BigDecimal;
import java.util.List;

public record ProductVariantResponse(
        Long id,
        String name,
        BigDecimal price,
        Integer stockQuantity,
        boolean isDefault,
        boolean active,

        List<String> imgUrls,
        List<ProductVariantAttributesResponse> attributes

) {
}
