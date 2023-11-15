package com.ecommerceboari.api.controller;

import com.ecommerceboari.api.dto.user.UserResponseDTO;
import com.ecommerceboari.api.service.UserService;
import com.ecommerceboari.api.util.UserCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

@ExtendWith(SpringExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Test
    @DisplayName("Returns logged in user details when getUser is called")
    void getUser_ReturnsUserDetails_WhenSuccessful() {
        UserResponseDTO expectedUser = UserCreator.createValidUserResponseDTO();

        Mockito.when(userService.findUserAuthenticated()).thenReturn(expectedUser);

        ResponseEntity<UserResponseDTO> responseEntity = userController.getUser();

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    @DisplayName("Returns page of users inside page object when getUsers is called")
    void getUsers_ReturnsPageOfUsers_WhenSuccessful() {
        Pageable pageable = PageRequest.of(0, 3);
        Page<UserResponseDTO> userDTOPage = new PageImpl<>(Collections.singletonList(UserCreator.createValidUserResponseDTO()));

        Mockito.when(userService.findAll(pageable)).thenReturn(userDTOPage);

        ResponseEntity<Page<UserResponseDTO>> responseEntity = userController.getUsers(pageable);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isNotNull();
    }
}