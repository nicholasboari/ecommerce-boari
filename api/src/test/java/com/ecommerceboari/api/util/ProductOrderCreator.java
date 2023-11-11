package com.ecommerceboari.api.util;

import com.ecommerceboari.api.dto.ProductOrderDTO;

import java.util.ArrayList;
import java.util.List;

public class ProductOrderCreator {

    public static ProductOrderDTO createValidProductOrderDTO() {
        return ProductOrderDTO.builder()
                .id(1L)
                .build();
    }

    public static List<ProductOrderDTO> createValidListOfProductOrderDTO() {
        ArrayList<ProductOrderDTO> list = new ArrayList<>();
        list.add(ProductOrderDTO.builder()
                .id(1L)
                .build());
        return list;
    }

}
