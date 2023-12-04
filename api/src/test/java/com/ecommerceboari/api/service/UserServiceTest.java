package com.ecommerceboari.api.service;

import com.ecommerceboari.api.auth.AuthenticationService;
import com.ecommerceboari.api.dto.response.OrderResponseDTO;
import com.ecommerceboari.api.dto.response.UserResponseDTO;
import com.ecommerceboari.api.exception.UserUnauthorizedRequestException;
import com.ecommerceboari.api.model.Order;
import com.ecommerceboari.api.model.User;
import com.ecommerceboari.api.repository.AddressRepository;
import com.ecommerceboari.api.repository.UserRepository;
import com.ecommerceboari.api.util.AddressCreator;
import com.ecommerceboari.api.util.OrderCreator;
import com.ecommerceboari.api.util.UserCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private ModelMapper modelMapper;

    private User user;

    @BeforeEach
    void setup() {
        user = UserCreator.createValidUser();

        Mockito.when(modelMapper.map(any(User.class), Mockito.eq(UserResponseDTO.class))).thenReturn(UserCreator.createValidUserResponseDTO());
        Mockito.when(modelMapper.map(any(UserResponseDTO.class), Mockito.eq(User.class))).thenReturn(UserCreator.createValidUser());
        Mockito.when(modelMapper.map(any(OrderResponseDTO.class), Mockito.eq(Order.class))).thenReturn(OrderCreator.createValidOrder());
    }

    @Test
    @DisplayName("Return a page of users when successful")
    void findAll_ShouldReturnAPageOfUsers_WhenSuccessful() {
        PageImpl<User> userPage = new PageImpl<>(Collections.singletonList(UserCreator.createValidUser()));
        Pageable pageable = PageRequest.of(0, 1);

        Mockito.when(userRepository.findAll(pageable)).thenReturn(userPage);

        Page<UserResponseDTO> result = userService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("Return a user when ID exists")
    void findById_ShouldReturnUser_WhenSuccessful() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));

        UserResponseDTO result = userService.findById(1L);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getUsername(), result.getUsername());
    }

    @Test
    @DisplayName("Update user address when user ID exists")
    void updateUserAddress_ShouldUpdateUserAddress_WhenIdExists() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(addressRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(AddressCreator.createValidAddress()));
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);

        userService.updateUserAddress(UserCreator.createValidUserResponseDTO());

        Mockito.verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Update user order when user ID exists")
    void updateUserOrder_ShouldUpdateUserOrder_WhenIdExists() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);

        userService.updateUserOrder(UserCreator.createValidUserResponseDTO());

        Mockito.verify(userRepository, times(1)).save(any(User.class));
    }


    @Test
    @DisplayName("Return authenticated user when successful")
    void findUserAuthenticated_ShouldReturnAuthenticatedUser_WhenSuccessful() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("nicholasboari", "123");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User simulatedUser = new User();
        simulatedUser.setId(1L);
        simulatedUser.setUsername("nicholasboari");

        Mockito.when(userRepository.findByUsername("nicholasboari")).thenReturn(Optional.of(simulatedUser));
        Mockito.when(authenticationService.returnUserAuthenticated()).thenReturn(user);
        UserResponseDTO result = userService.findUserAuthenticated();

        assertNotNull(result);
        assertEquals(simulatedUser.getId(), result.getId());
        assertEquals(simulatedUser.getUsername(), result.getUsername());
    }


    @Test
    @DisplayName("Throws UserUnauthorizedRequestException when user is not found in the repository")
    void returnUserAuthenticated_ThrowsException_WhenUserNotFound() {
        Mockito.when(authenticationService.returnUserAuthenticated()).thenThrow(new UserUnauthorizedRequestException("User not authenticated!"));

        assertThatThrownBy(() -> userService.findUserAuthenticated())
                .isInstanceOf(UserUnauthorizedRequestException.class)
                .hasMessage("User not authenticated!");
    }

}