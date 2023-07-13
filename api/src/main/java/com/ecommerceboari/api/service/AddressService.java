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
        return addressRepository.findAll().stream().map(address -> modelMapper.map(address, AddressDTO.class)).toList();
    }

    public Page<AddressDTO> findPaged(Pageable pageable) {
        Page<Address> addresses = addressRepository.findAll(pageable);
        return addresses.map(address -> modelMapper.map(address, AddressDTO.class));
    }

    public AddressDTO findById(Long id) {
        Address address = addressRepository.findById(id).orElseThrow(() -> new BadRequestException("Address not found!"));
        return modelMapper.map(address, AddressDTO.class);
    }

    @Transactional
    public AddressDTO save(AddressDTO addressDTO) {
        Address address = modelMapper.map(addressDTO, Address.class);
        Address addressSaved = addressRepository.save(address);
        return modelMapper.map(addressSaved, AddressDTO.class);
    }

    @Transactional
    public AddressDTO update(AddressDTO addressDTO, Long id) {
        findById(id);
        Address address = modelMapper.map(addressDTO, Address.class);
        address.setId(id);
        AddressDTO addressSaved = modelMapper.map(address, AddressDTO.class);
        return save(addressSaved);
    }

    @Transactional
    public void delete(Long id) {
        AddressDTO address = findById(id);
        addressRepository.deleteById(address.getId());
    }

}
