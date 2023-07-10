package com.ecommerceboari.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AddressDTO(
        Long id,
        @NotNull
        @Pattern(regexp = "^\\d{5}-\\d{3}$", message = "invalid CEP")
        String cep,
        @NotNull
        @Size(min = 2, message = "size needs to be greater than 2")
        String country,
        @NotNull
        @Size(min = 2, max = 2, message = "size needs to be 2")
        String state,
        @NotNull
        @Size(min = 2, message = "size needs to be greater than 2")
        String street,
        @NotNull
        @Size(min = 2, message = "size needs to be greater than 2")
        String neighborhood) {

}
