package com.ecommerceboari.api.util;

import com.ecommerceboari.api.dto.CategoryDTO;
import com.ecommerceboari.api.model.Category;

public class CategoryCreator {

    public static Category createValidCategory() {
        return Category.builder()
                .id(1L)
                .name("Category A")
                .build();
    }

    public static CategoryDTO createValidCategoryDTO() {
        return CategoryDTO.builder()
                .id(1L)
                .name("Category A")
                .build();
    }
}
