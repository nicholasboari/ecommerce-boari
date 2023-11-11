package com.ecommerceboari.api.util;

import com.ecommerceboari.api.dto.AddressDTO;
import com.ecommerceboari.api.model.Address;

public class AddressCreator {

    public static Address createValidAddress() {
        return Address.builder()
                .id(1L)
                .street("123 Main Street")
                .build();
    }

    public static AddressDTO createValidAddressDTO() {
        return AddressDTO.builder()
                .id(1L)
                .street("123 Main Street")
                .build();
    }
}
