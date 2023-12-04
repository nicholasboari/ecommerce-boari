package com.ecommerceboari.api.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.ecommerceboari.api.dto.AddressDTO;
import com.ecommerceboari.api.model.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDTO {

    @JsonIgnore
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String username;
    private LocalDateTime createdAt;
    @JsonIgnore
    private Role role;
    private AddressDTO address;
    @JsonProperty("orders")
    private List<OrderResponseDTO> order;
}
