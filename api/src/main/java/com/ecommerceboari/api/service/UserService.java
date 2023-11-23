package com.ecommerceboari.api.service;

import com.ecommerceboari.api.auth.AuthenticationService;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
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
            Address address = userResponseDTO.getAddress().getId() != null ? addressRepository.findById(userResponseDTO
                    .getAddress()
                    .getId()).orElseThrow(() -> new BadRequestException("Address not found")) : null;
            if (address != null) {
                user.setAddress(address);
            } else {
                Address newAddress = modelMapper.map(userResponseDTO.getAddress(), Address.class);
                user.setAddress(newAddress);
            }
        }

        User userSaved = userRepository.save(user);
        LOGGER.info("Updating {} into the database", userSaved);
    }

    public void updateUserOrder(UserResponseDTO userResponseDTO) {
        User user = userRepository.findById(userResponseDTO.getId())
                .orElseThrow(() -> new BadRequestException("User not found"));
        try {
            userResponseDTO.getOrder().forEach(orderDTO -> {
                Order orderMapped = modelMapper.map(orderDTO, Order.class);
                if (!user.getOrder().contains(orderMapped)) {
                    List<Product> products = new ArrayList<>();
                    orderDTO.getProducts().forEach(productDTO -> {
                        Product product = modelMapper.map(productDTO, Product.class);
                        products.add(product);
                    });

                    orderMapped.setProducts(products);
                    user.getOrder().add(orderMapped);
                }
            });

            User userSaved = userRepository.save(user);
            LOGGER.info("Updating {} into the database", userSaved);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UserResponseDTO findUserAuthenticated() {
        User user = authenticationService.returnUserAuthenticated();
        return modelMapper.map(user, UserResponseDTO.class);
    }

}
