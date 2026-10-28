package com.nuvora.nuvora.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthRequest(
        @NotBlank(message = "Email obrigatório")
        @Email(message = "Email inválido")
        @Size(max = 100, message = "Email muito longo")
        String email,

        @NotBlank(message = "Senha obrigatória")
        @Size(min = 8, max = 64, message = "Senha deve ter entre 8 e 64 caracteres")
        String password
) {
}
