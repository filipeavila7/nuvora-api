package com.nulvora.nulvora.productImg.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProductImgRequest(

        @NotBlank
        @Size(max = 200)
        String imageUrl,

        boolean isPrimary
) {}
