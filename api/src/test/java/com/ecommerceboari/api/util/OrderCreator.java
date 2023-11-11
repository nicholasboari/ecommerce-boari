package com.ecommerceboari.api.util;

import com.ecommerceboari.api.dto.order.OrderRequestDTO;
import com.ecommerceboari.api.dto.order.OrderResponseDTO;
import com.ecommerceboari.api.model.Order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderCreator {

    public static Order createValidOrder() {
        return Order.builder()
                .id(1L)
                .total(100D)
                .moment(LocalDateTime.now())
                .products(ProductCreator.createValidListOfProduct())
                .build();
    }

    public static List<Order> createValidListOfOrder() {
        ArrayList<Order> list = new ArrayList<>();
        list.add(Order.builder()
                .id(1L)
                .total(100D)
                .moment(LocalDateTime.now())
                .products(ProductCreator.createValidListOfProduct())
                .build());
        return list;
    }


    public static OrderResponseDTO createValidOrderResponseDTO() {
        return OrderResponseDTO.builder()
                .id(1L)
                .total(100D)
                .moment(LocalDateTime.now())
                .products(ProductCreator.createValidListOfProductDTO())
                .build();
    }

    public static List<OrderResponseDTO> createValidListOfOrderResponseDTO() {
        ArrayList<OrderResponseDTO> list = new ArrayList<>();
        list.add(createValidOrderResponseDTO());
        return list;
    }

    public static OrderRequestDTO createValidOrderRequestDTO() {
        return OrderRequestDTO.builder()
                .id(1L)
                .moment(LocalDateTime.now())
                .products(ProductOrderCreator.createValidListOfProductOrderDTO())
                .build();
    }
}
