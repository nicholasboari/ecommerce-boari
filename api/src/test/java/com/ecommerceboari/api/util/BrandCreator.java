package com.ecommerceboari.api.util;

import com.ecommerceboari.api.dto.BrandDTO;
import com.ecommerceboari.api.model.Brand;

public class BrandCreator {

    public static Brand createValidBrand() {
        return Brand.builder()
                .id(1L)
                .name("Brand A")
                .build();
    }

    public static BrandDTO createValidBrandDTO() {
        return BrandDTO.builder()
                .id(1L)
                .name("Brand A")
                .build();
    }
}
