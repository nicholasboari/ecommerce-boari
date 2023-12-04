package com.ecommerceboari.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegisterRequestDTO {

    @NotBlank(message = "The first name is mandatory")
    private String firstName;
    @NotBlank(message = "The last name is mandatory")
    private String lastName;
    @NotBlank(message = "The phone number is mandatory")
    private String phone;
    @Email(message = "Email must be valid")
    private String email;
    @NotBlank(message = "Password is mandatory")
    private String password;
    @NotBlank(message = "Username is mandatory")
    private String username;
}
