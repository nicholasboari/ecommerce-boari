package com.ecommerceboari.api.service;

import com.ecommerceboari.api.dto.AddressDTO;
import com.ecommerceboari.api.exception.BadRequestException;
import com.ecommerceboari.api.model.Address;
import com.ecommerceboari.api.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    public List<AddressDTO> findAll() {
        return addressRepository.findAll().stream().map(address -> new AddressDTO(
                        address.getId(),
                        address.getNeighborhood(),
                        address.getCep(),
                        address.getCountry(),
                        address.getState(),
                        address.getStreet()))
                .toList();
    }

    public Page<AddressDTO> findPaged(Pageable pageable) {
        Page<Address> addresses = addressRepository.findAll(pageable);
        return addresses.map(address -> new AddressDTO(
                address.getId(),
                address.getCep(),
                address.getCountry(),
                address.getState(),
                address.getStreet(),
                address.getNeighborhood()));
    }

    public AddressDTO findById(Long id) {
        Address address = addressRepository.findById(id).orElseThrow(() -> new BadRequestException("Address not found!"));
        return new AddressDTO(address.getId(),
                address.getCep(),
                address.getCountry(),
                address.getState(),
                address.getStreet(),
                address.getNeighborhood());
    }

    @Transactional
    public AddressDTO save(AddressDTO addressDTO) {
        Address address = Address.builder()
                .id(addressDTO.id())
                .cep(addressDTO.cep())
                .state(addressDTO.state())
                .country(addressDTO.country())
                .neighborhood(addressDTO.neighborhood())
                .street(addressDTO.street())
                .build();

        Address addressSaved = addressRepository.save(address);
        return new AddressDTO(addressSaved.getId(),
                addressSaved.getCep(),
                addressSaved.getCountry(),
                addressSaved.getState(),
                addressSaved.getStreet(),
                addressSaved.getNeighborhood());
    }

    @Transactional
    public AddressDTO update(AddressDTO addressDTO, Long id) {
        AddressDTO dto = findById(id);
        Address address = Address.builder()
                .id(dto.id())
                .cep(addressDTO.cep())
                .state(addressDTO.state())
                .country(addressDTO.country())
                .neighborhood(addressDTO.neighborhood())
                .street(addressDTO.street())
                .build();

        AddressDTO addressSaved = new AddressDTO(address.getId(),
                address.getCep(),
                address.getCountry(),
                address.getState(),
                address.getStreet(),
                address.getNeighborhood());
        return save(addressSaved);
    }

    @Transactional
    public void delete(Long id) {
        AddressDTO address = findById(id);
        addressRepository.deleteById(address.id());
    }
}
