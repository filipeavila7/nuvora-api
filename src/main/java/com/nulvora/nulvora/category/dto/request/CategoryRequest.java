package com.nulvora.nulvora.category.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
        @NotBlank
        @Size(max = 100, message = "O nome do produto deve ter no máximo 100 caracters")
        String name
) {
}
