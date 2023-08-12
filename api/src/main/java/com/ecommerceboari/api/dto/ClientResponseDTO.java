package com.ecommerceboari.api.dto;

import com.ecommerceboari.api.model.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String username;
    private Address address;
}
