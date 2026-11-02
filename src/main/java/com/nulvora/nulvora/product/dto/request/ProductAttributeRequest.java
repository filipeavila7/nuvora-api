package com.nulvora.nulvora.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ProductAttributeRequest(
        @NotBlank
        @Size(max = 100, min = 2, message = "No mínimo 2 caracteres e no máximo 100")
        String name,

        @Size(min = 1)
        @NotNull
        List<@NotBlank @Size(max = 100, min = 2) String> options
) {
}
