package com.ecommerceboari.api.service;

import com.ecommerceboari.api.auth.AuthenticationService;
import com.ecommerceboari.api.dto.ProductDTO;
import com.ecommerceboari.api.dto.user.UserResponseDTO;
import com.ecommerceboari.api.exception.BadRequestException;
import com.ecommerceboari.api.model.Address;
import com.ecommerceboari.api.model.Order;
import com.ecommerceboari.api.model.Product;
import com.ecommerceboari.api.model.User;
import com.ecommerceboari.api.repository.AddressRepository;
import com.ecommerceboari.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
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

    public void updateUserAddress(UserResponseDTO userResponseDTO) {
        User user = userRepository.findById(userResponseDTO.getId())
                .orElseThrow(() -> new BadRequestException("User not found"));

        if (userResponseDTO.getAddress() != null) {
            Address address = userResponseDTO.getAddress().getId() != null ?
                    addressRepository.findById(userResponseDTO
                            .getAddress()
                            .getId()).orElseThrow(() -> new BadRequestException("Address not found")) : null;
            if (address != null) {
                user.setAddress(address);
            } else {
                Address newAddress = modelMapper.map(userResponseDTO.getAddress(), Address.class);
                user.setAddress(newAddress);
            }
        }

        userRepository.save(user);
    }

    public void updateUserOrder(UserResponseDTO userResponseDTO) {
        User user = userRepository.findById(userResponseDTO.getId())
                .orElseThrow(() -> new BadRequestException("User not found"));

        user.getOrder().clear();

        userResponseDTO.getOrder().forEach(order -> {
            Order orderMapped = modelMapper.map(order, Order.class);
            user.getOrder().add(orderMapped);
        });

        int lastIndex = userResponseDTO.getOrder().size() - 1;
        for (ProductDTO productDTO : userResponseDTO.getOrder().get(lastIndex).getProducts()) {
            Product product = modelMapper.map(productDTO, Product.class);
            user.getOrder().get(lastIndex).getProducts().add(product);
        }
        userRepository.save(user);
    }

    public UserResponseDTO findUserAuthenticated() {
        User user = authenticationService.returnUserAuthenticated();
        return modelMapper.map(user, UserResponseDTO.class);
    }

}
