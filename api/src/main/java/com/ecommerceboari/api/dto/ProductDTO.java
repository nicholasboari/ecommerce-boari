package com.ecommerceboari.api.dto;

import com.ecommerceboari.api.model.Brand;
import com.ecommerceboari.api.model.Category;
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
    private Brand brand;
    private Category category;
}
