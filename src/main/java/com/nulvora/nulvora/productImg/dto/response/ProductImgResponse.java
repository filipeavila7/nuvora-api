package com.nulvora.nulvora.productImg.dto.response;

public record ProductImgResponse(
        Long id,
        String imageUrl,
        boolean primary
) {}