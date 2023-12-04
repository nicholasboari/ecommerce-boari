package com.ecommerceboari.api.service;

import com.ecommerceboari.api.dto.AddressDTO;
import com.ecommerceboari.api.dto.response.UserResponseDTO;
import com.ecommerceboari.api.model.Address;
import com.ecommerceboari.api.repository.AddressRepository;
import com.ecommerceboari.api.util.AddressCreator;
import com.ecommerceboari.api.util.UserCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class AddressServiceTest {

    @InjectMocks
    private AddressService addressService;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private UserService userService;

    private List<Address> addressList;
    private Address address;
    private AddressDTO addressDTO;

    @BeforeEach
    void setup() {
        addressList = List.of(AddressCreator.createValidAddress());
        address = AddressCreator.createValidAddress();
        addressDTO = AddressCreator.createValidAddressDTO();

        Mockito.when(modelMapper.map(Mockito.any(Address.class), Mockito.eq(AddressDTO.class))).thenReturn(AddressCreator.createValidAddressDTO());
        Mockito.when(modelMapper.map(Mockito.any(AddressDTO.class), Mockito.eq(Address.class))).thenReturn(AddressCreator.createValidAddress());
    }

    @Test
    @DisplayName("Return a page of addresses when successful")
    void findPaged_ShouldReturnAPageOfAddresses_WhenSuccessful() {
        PageImpl<Address> addressPage = new PageImpl<>(Collections.singletonList(AddressCreator.createValidAddress()));
        Pageable pageable = PageRequest.of(0, 1);

        Mockito.when(addressRepository.findAll(pageable)).thenReturn(addressPage);

        Page<AddressDTO> result = addressService.findPaged(pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("Return a list of addresses when successful")
    void findAll_ShouldReturnAListOfAddresses_WhenSuccessful() {
        Mockito.when(addressRepository.findAll()).thenReturn(addressList);

        List<AddressDTO> result = addressService.findAll();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Return an address when ID exists")
    void findById_ShouldReturnAddress_WhenSuccessful() {
        Mockito.when(addressRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(address));

        AddressDTO result = addressService.findById(1L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(address.getId(), result.getId());
        Assertions.assertEquals(address.getStreet(), result.getStreet());
    }

    @Test
    @DisplayName("Save a new address when ID is null")
    void save_ShouldCreateAddress_WhenSuccessful() {
        Mockito.doNothing().when(userService).updateUserAddress(Mockito.any(UserResponseDTO.class));
        Mockito.when(addressRepository.save(Mockito.any(Address.class))).thenReturn(AddressCreator.createValidAddress());

        AddressDTO createdAddress = addressService.save(addressDTO, UserCreator.createUserResponseDTOWithoutAddress());

        Assertions.assertNotNull(createdAddress);
        Assertions.assertEquals("123 Main Street", createdAddress.getStreet());
    }
}