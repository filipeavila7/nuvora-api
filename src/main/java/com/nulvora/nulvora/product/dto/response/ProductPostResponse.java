package com.nulvora.nulvora.product.dto.response;


import java.time.LocalDateTime;
import java.util.List;

public record ProductPostResponse(
        Long id,
        String name,
        String categoryName,
        String imageUrl,
        boolean isActive,
        LocalDateTime createdAt,
        List<ProductAttributeResponse> attributes

) {
}
