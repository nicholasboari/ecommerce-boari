package com.ecommerceboari.api.dto.user;

import com.ecommerceboari.api.model.Address;
import com.ecommerceboari.api.model.Order;
import com.ecommerceboari.api.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String username;
    private LocalDateTime createdAt;
    private Role role;
    private Address address;
    private List<Order> order;
}
