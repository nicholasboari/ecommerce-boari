package com.ecommerceboari.api.util;

import com.ecommerceboari.api.model.Category;

public class CategoryCreator {

    public static Category createValidCategory() {
        return Category.builder()
                .id(1L)
                .name("Category A")
                .build();
    }
}
