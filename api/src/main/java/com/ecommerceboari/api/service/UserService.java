package com.ecommerceboari.api.service;

import com.ecommerceboari.api.auth.AuthenticationService;
import com.ecommerceboari.api.dto.user.UserResponseDTO;
import com.ecommerceboari.api.exception.BadRequestException;
import com.ecommerceboari.api.model.Address;
import com.ecommerceboari.api.model.Order;
import com.ecommerceboari.api.model.User;
import com.ecommerceboari.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final ModelMapper modelMapper;

    public UserResponseDTO findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new BadRequestException("User not found"));
        return modelMapper.map(user, UserResponseDTO.class);
    }

    public UserResponseDTO save(UserResponseDTO userResponseDTO) {
        User user = userRepository.findById(userResponseDTO.getId()).orElseThrow(() -> new BadRequestException("User not found"));

        userResponseDTO.getOrder().stream().map(order -> {
            Order mapped = modelMapper.map(order, Order.class);
            return user.getOrder().add(mapped);
        });

        Address address = modelMapper.map(userResponseDTO.getAddress(), Address.class);
        user.setAddress(address);
        User userSaved = userRepository.save(user);
        return modelMapper.map(userSaved, UserResponseDTO.class);
    }

    public UserResponseDTO findUserAuthenticated() {
        User user = authenticationService.returnUserAuthenticated();
        return modelMapper.map(user, UserResponseDTO.class);
    }
}
