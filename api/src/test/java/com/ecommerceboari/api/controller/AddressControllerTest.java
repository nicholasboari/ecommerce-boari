package com.ecommerceboari.api.controller;

import com.ecommerceboari.api.dto.AddressDTO;
import com.ecommerceboari.api.dto.response.UserResponseDTO;
import com.ecommerceboari.api.service.AddressService;
import com.ecommerceboari.api.service.UserService;
import com.ecommerceboari.api.util.AddressCreator;
import com.ecommerceboari.api.util.UserCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
class AddressControllerTest {

    @InjectMocks
    private AddressController addressController;

    @Mock
    private AddressService addressService;

    @Mock
    private UserService userService;

    private Pageable pageable;

    @BeforeEach
    void setup() {
        pageable = PageRequest.of(0, 3);
        Page<AddressDTO> addressDTOPage = new PageImpl<>(Collections.singletonList(AddressCreator.createValidAddressDTO()));
        Mockito.when(this.addressService.findPaged(ArgumentMatchers.any(Pageable.class))).thenReturn(addressDTOPage);
    }

    @Test
    @DisplayName("Returns page of addresses inside page object when successful")
    void findPaged_ReturnsPageOfAddresses_WhenSuccessful() {
        Page<AddressDTO> addressPage = addressController.findPaged(pageable).getBody();

        Assertions.assertThat(addressPage).isNotNull();
    }

    @Test
    @DisplayName("Returns a list of addresses when findAll is called")
    void findAll_ReturnsListOfAddresses_WhenSuccessful() {
        List<AddressDTO> expectedAddresses = Arrays.asList(
                AddressCreator.createValidAddressDTO(),
                AddressCreator.createValidAddressDTO(),
                AddressCreator.createValidAddressDTO()
        );

        Mockito.when(this.addressService.findAll()).thenReturn(expectedAddresses);

        ResponseEntity<List<AddressDTO>> responseEntity = addressController.findAll();

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isNotNull();
        Assertions.assertThat(responseEntity.getBody()).hasSize(expectedAddresses.size());
    }

    @Test
    @DisplayName("Returns an address when findById is called with a valid ID")
    void findById_ReturnsAddress_WhenSuccessful() {
        Long validId = 1L;
        AddressDTO expectedAddress = AddressCreator.createValidAddressDTO();

        Mockito.when(this.addressService.findById(validId)).thenReturn(expectedAddress);

        ResponseEntity<AddressDTO> responseEntity = addressController.findById(validId);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isNotNull();
        Assertions.assertThat(responseEntity.getBody().getId()).isEqualTo(validId);
    }

    @Test
    @DisplayName("Creates a new address when save is called with a valid DTO")
    void save_CreatesNewAddress_WhenSuccessful() {
        AddressDTO validAddressDTO = AddressCreator.createValidAddressDTO();
        UserResponseDTO userAuthenticated = UserCreator.createValidUserResponseDTO();
        AddressDTO expectedAddress = AddressCreator.createValidAddressDTO();

        Mockito.when(this.userService.findUserAuthenticated()).thenReturn(userAuthenticated);
        Mockito.when(this.addressService.save(validAddressDTO, userAuthenticated)).thenReturn(expectedAddress);

        ResponseEntity<AddressDTO> responseEntity = addressController.save(validAddressDTO);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(responseEntity.getBody()).isNotNull();
        Assertions.assertThat(responseEntity.getBody().getId()).isEqualTo(expectedAddress.getId());
    }

    @Test
    @DisplayName("Updates an address when update is called with a valid DTO and ID")
    void update_UpdatesAddress_WhenSuccessful() {
        Long validId = 1L;
        AddressDTO validAddressDTO = AddressCreator.createValidAddressDTO();
        UserResponseDTO userAuthenticated = UserCreator.createValidUserResponseDTO();
        AddressDTO expectedAddress = AddressCreator.createValidAddressDTO();

        Mockito.when(this.userService.findUserAuthenticated()).thenReturn(userAuthenticated);
        Mockito.when(this.addressService.update(validAddressDTO, validId, userAuthenticated)).thenReturn(expectedAddress);

        ResponseEntity<AddressDTO> responseEntity = addressController.update(validAddressDTO, validId);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(responseEntity.getBody()).isNotNull();
        Assertions.assertThat(responseEntity.getBody().getId()).isEqualTo(expectedAddress.getId());
    }
}