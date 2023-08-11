package com.ecommerceboari.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {

    private Long id;

    private String name;
    private Long price;
    private String imageUrl;
    private BrandDTO brand;
    private CategoryDTO category;
}
