package com.ecommerceboari.api.service;

import com.ecommerceboari.api.dto.AddressDTO;
import com.ecommerceboari.api.exception.BadRequestException;
import com.ecommerceboari.api.model.Address;
import com.ecommerceboari.api.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;

    public List<AddressDTO> findAll() {
        return addressRepository.findAll().stream().map(AddressService::buildDto).toList();
    }

    public Page<AddressDTO> findPaged(Pageable pageable) {
        Page<Address> addresses = addressRepository.findAll(pageable);
        return addresses.map(AddressService::buildDto);
    }

    public AddressDTO findById(Long id) {
        Address address = addressRepository.findById(id).orElseThrow(() -> new BadRequestException("Address not found!"));
        return buildDto(address);
    }

    @Transactional
    public AddressDTO save(AddressDTO addressDTO) {
        Address address = buildModel(addressDTO);
        Address addressSaved = addressRepository.save(address);
        return modelMapper.map(addressSaved, AddressDTO.class);
    }

    @Transactional
    public AddressDTO update(AddressDTO addressDTO, Long id) {
        findById(id);
        Address address = buildModel(addressDTO);
        address.setId(id);
        AddressDTO addressSaved = buildDto(address);
        return save(addressSaved);
    }

    @Transactional
    public void delete(Long id) {
        AddressDTO address = findById(id);
        addressRepository.deleteById(address.getId());
    }

    public static Address buildModel(AddressDTO addressDTO) {
        return Address.builder()
                .id(addressDTO.getId())
                .street(addressDTO.getStreet())
                .neighborhood(addressDTO.getNeighborhood())
                .country(addressDTO.getCountry())
                .state(addressDTO.getState())
                .cep(addressDTO.getCep())
                .build();
    }

    public static AddressDTO buildDto(Address address) {
        return AddressDTO.builder()
                .id(address.getId())
                .street(address.getStreet())
                .cep(address.getCep())
                .country(address.getCountry())
                .neighborhood(address.getNeighborhood())
                .state(address.getState())
                .build();
    }
}
