package com.ecommerceboari.api.service;

import com.ecommerceboari.api.auth.AuthenticationService;
import com.ecommerceboari.api.dto.AddressDTO;
import com.ecommerceboari.api.dto.response.UserResponseDTO;
import com.ecommerceboari.api.exception.BadRequestException;
import com.ecommerceboari.api.exception.UserForbiddenRequestException;
import com.ecommerceboari.api.model.Address;
import com.ecommerceboari.api.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddressService.class);
    private final AddressRepository addressRepository;
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public List<AddressDTO> findAll() {
        return addressRepository.findAll().stream().map(address -> modelMapper.map(address, AddressDTO.class)).toList();
    }

    public Page<AddressDTO> findPaged(Pageable pageable) {
        Page<Address> addresses = addressRepository.findAll(pageable);
        return addresses.map(address -> modelMapper.map(address, AddressDTO.class));
    }

    public AddressDTO findById(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Address not found!"));
        return modelMapper.map(address, AddressDTO.class);
    }

    public AddressDTO save(AddressDTO addressDTO, UserResponseDTO userResponseDTO) {
        if (userResponseDTO.getAddress() != null) {
            throw new BadRequestException("Address already exist");
        }

        Address address = modelMapper.map(addressDTO, Address.class);
        Address addressSaved = addressRepository.save(address);
        userResponseDTO.setAddress(modelMapper.map(addressSaved, AddressDTO.class));

        userService.updateUserAddress(userResponseDTO);

        LOGGER.info("Inserting {} to the database", addressSaved);
        return modelMapper.map(addressSaved, AddressDTO.class);
    }

    public AddressDTO update(AddressDTO addressDTO, Long addressId, UserResponseDTO userResponseDTO) {
        if(!userResponseDTO.getAddress().getId().equals(addressId)){
            throw new UserForbiddenRequestException("You don't have permission to access this resource");
        }
        Address address = modelMapper.map(addressDTO, Address.class);
        address.setId(addressId);
        addressRepository.save(address);
        return modelMapper.map(address, AddressDTO.class);
    }

}
