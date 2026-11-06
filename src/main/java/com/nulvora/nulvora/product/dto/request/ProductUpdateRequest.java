package com.nulvora.nulvora.product.dto.request;

public record ProductUpdateRequest(
        String name,
        String description,
        String imageUrl,
        Boolean active,
        Long categoryId
) {
}
