package com.ecommerceboari.api.dto.user;

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
public class UserLoginRequestDTO {

    @Email(message = "Email must be valid")
    private String email;
    @NotBlank(message = "Username is mandatory")
    private String password;
}
