package com.nulvora.nulvora.address.dto.request;


import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PutAddressRequest(

        @Size(max = 150, message = "A rua deve ter no máximo 150 caracteres")
        String street,


        @Size(max = 20, message = "O número deve ter no máximo 20 caracteres")
        String number,


        @Size(max = 100, message = "O bairro deve ter no máximo 100 caracteres")
        String neighborhood,


        @Size(max = 100, message = "A cidade deve ter no máximo 100 caracteres")
        String city,


        @Pattern(
                regexp = "^[A-Za-z]{2}$",
                message = "O estado deve possuir uma UF válida com 2 letras"
        )
        String state,


        @Pattern(
                regexp = "^\\d{5}-?\\d{3}$",
                message = "O CEP deve possuir 8 dígitos"
        )
        String zipCode,

        @Size(max = 100, message = "O complemento deve ter no máximo 100 caracteres")
        String complement
) {
}
