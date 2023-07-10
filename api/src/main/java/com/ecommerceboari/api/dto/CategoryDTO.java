package com.ecommerceboari.api.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDTO {

    private Long id;
    @Size(min = 2, max = 14, message = "size needs to be greater than 2")
    private String name;
    private String imageUrl;
}
