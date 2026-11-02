package com.nulvora.nulvora.product.dto.response;


import java.util.List;

public record ProductPostResponse(
        String name,
        String categoryName,
        String imageUrl,
        boolean isActive,
        List<ProductAttributeResponse> attributes

) {
}
