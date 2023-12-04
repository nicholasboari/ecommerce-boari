package com.ecommerceboari.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegisterResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String username;
}
