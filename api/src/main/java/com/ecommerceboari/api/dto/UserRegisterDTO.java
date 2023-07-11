package com.ecommerceboari.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegisterDTO {

    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String password;
    private String username;
}
