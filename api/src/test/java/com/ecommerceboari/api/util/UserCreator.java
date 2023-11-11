package com.ecommerceboari.api.util;

import com.ecommerceboari.api.dto.CategoryDTO;
import com.ecommerceboari.api.dto.user.UserResponseDTO;
import com.ecommerceboari.api.model.User;

public class UserCreator {

    public static UserResponseDTO createValidUserResponseDTO() {
        return UserResponseDTO.builder()
                .id(1L)
                .firstName("Nicholas")
                .lastName("Boari")
                .order(OrderCreator.createValidListOfOrderResponseDTO())
                .address(AddressCreator.createValidAddressDTO())
                .build();
    }

    public static UserResponseDTO createUserResponseDTOWithoutAddress() {
        return UserResponseDTO.builder()
                .id(1L)
                .firstName("Nicholas")
                .lastName("Boari")
                .order(OrderCreator.createValidListOfOrderResponseDTO())
                .build();
    }

    public static CategoryDTO createValidCategoryDTO() {
        return CategoryDTO.builder()
                .id(1L)
                .name("Category A")
                .build();
    }

    public static User createValidUser() {
        return User.builder()
                .id(1L)
                .firstName("Nicholas")
                .lastName("Boari")
                .order(OrderCreator.createValidListOfOrder())
                .build();
    }
}
