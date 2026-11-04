package com.nulvora.nulvora.product.dto.response;

import java.util.List;

public record ProductAttributeResponse(
        Long id,
        String name,
        List<String> options
) {}