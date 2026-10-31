package com.nulvora.nulvora.product.dto.response;



public record ProductPostResponse(
        String name,
        String categoryName,
        String imageUrl,
        boolean isActive
) {
}
