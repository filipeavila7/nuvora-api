package com.nulvora.nulvora.product.dto.response;

public record ProductUpdateResponse(
        Long id,
        String name,
        String description,
        String imageUrl,
        boolean active,
        String categoryName
) {
}
