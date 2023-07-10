package com.ecommerceboari.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoryDTO(
        Long id,
        @NotNull
        @Size(min = 2, max = 14, message = "size needs to be greater than 2 and less than 14") String name,
        String imageUrl) {
}
