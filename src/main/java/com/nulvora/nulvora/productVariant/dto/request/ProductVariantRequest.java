package com.nulvora.nulvora.productVariant.dto.request;

import java.math.BigDecimal;

public record ProductVariantRequest(
        String sku,
        String name,
        BigDecimal price,
        Integer stockQuantity

) {
}
