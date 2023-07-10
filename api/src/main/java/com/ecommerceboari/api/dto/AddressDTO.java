package com.ecommerceboari.api.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressDTO {
    private Long id;
    @Pattern(regexp = "^\\d{5}-\\d{3}$", message = "invalid CEP")
    private String cep;
    @Size(min = 2, message = "size needs to be greater than 2")
    private String country;
    @Size(min = 2, max = 2, message = "size needs to be 2")
    private String state;
    @Size(min = 2, message = "size needs to be greater than 2")
    private String street;
    @Size(min = 2, message = "size needs to be greater than 2")
    private String neighborhood;
}

