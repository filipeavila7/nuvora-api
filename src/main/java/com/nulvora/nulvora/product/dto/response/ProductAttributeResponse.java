package com.nulvora.nulvora.product.dto.response;

import java.util.List;

public record ProductAttributeResponse(
        String name,
        List<String> options
) {}