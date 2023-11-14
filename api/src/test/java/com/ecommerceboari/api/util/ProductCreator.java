package com.ecommerceboari.api.util;

import com.ecommerceboari.api.dto.ProductDTO;
import com.ecommerceboari.api.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductCreator {

    public static Product createValidProduct() {
        return Product.builder()
                .id(1L)
                .name("Iphone X")
                .price(1000L)
                .imageUrl("image1.png")
                .brand(BrandCreator.createValidBrand())
                .category(CategoryCreator.createValidCategory())
                .build();
    }

    public static List<Product> createValidListOfProduct() {
        ArrayList<Product> list = new ArrayList<>();
        list.add(ProductCreator.createValidProduct());
        return list;
    }

    public static ProductDTO createValidProductDTO() {
        return ProductDTO.builder()
                .id(1L)
                .name("Iphone X")
                .imageUrl("image1.png")
                .brand(BrandCreator.createValidBrandDTO())
                .price(1000L)
                .category(CategoryCreator.createValidCategoryDTO())
                .build();
    }

    public static List<ProductDTO> createValidListOfProductDTO() {
        ArrayList<ProductDTO> list = new ArrayList<>();
        list.add(createValidProductDTO());
        return list;
    }
}
