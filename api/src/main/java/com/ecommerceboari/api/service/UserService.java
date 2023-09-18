package com.ecommerceboari.api.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ecommerceboari.api.auth.AuthenticationService;
import com.ecommerceboari.api.dto.user.UserResponseDTO;
import com.ecommerceboari.api.exception.BadRequestException;
import com.ecommerceboari.api.model.Order;
import com.ecommerceboari.api.model.User;
import com.ecommerceboari.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final ModelMapper modelMapper;

    public Page<UserResponseDTO> findAll(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(user -> modelMapper.map(user, UserResponseDTO.class));
    }

    public UserResponseDTO findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new BadRequestException("User not found"));
        return modelMapper.map(user, UserResponseDTO.class);
    }

    public UserResponseDTO save(UserResponseDTO userResponseDTO) {
        User user = userRepository.findById(userResponseDTO.getId())
                .orElseThrow(() -> new BadRequestException("User not found"));

        userResponseDTO.getOrder().forEach(order -> {
            Order mapped = modelMapper.map(order, Order.class);
            user.getOrder().add(mapped);
        });

        User userSaved = userRepository.save(user);
        return modelMapper.map(userSaved, UserResponseDTO.class);
    }

    public UserResponseDTO findUserAuthenticated() {
        User user = authenticationService.returnUserAuthenticated();
        return modelMapper.map(user, UserResponseDTO.class);
    }
}
