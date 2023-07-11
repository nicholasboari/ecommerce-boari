package com.ecommerceboari.api.service;

import com.ecommerceboari.api.auth.AuthenticationService;
import com.ecommerceboari.api.dto.UserResponseDTO;
import com.ecommerceboari.api.model.User;
import com.ecommerceboari.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    public UserResponseDTO findUserAuthenticated() {
        User user = authenticationService.returnUserAuthenticated();
        UserResponseDTO userResponse = UserResponseDTO.builder().build();
        BeanUtils.copyProperties(user, userResponse);
        return userResponse;
    }
}
