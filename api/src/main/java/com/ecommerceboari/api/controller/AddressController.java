package com.ecommerceboari.api.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerceboari.api.dto.AddressDTO;
import com.ecommerceboari.api.dto.user.UserResponseDTO;
import com.ecommerceboari.api.service.AddressService;
import com.ecommerceboari.api.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/addresses")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AddressController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddressController.class);
    private final AddressService addressService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<AddressDTO>> findPaged(Pageable pageable) {
        Page<AddressDTO> paged = addressService.findPaged(pageable);
        LOGGER.info("Received request to fetch all addresses paginated");
        return ResponseEntity.ok().body(paged);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AddressDTO>> findAll() {
        List<AddressDTO> addressList = addressService.findAll();
        LOGGER.info("Received request to fetch all addresses");
        return ResponseEntity.ok().body(addressList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDTO> findById(@PathVariable Long id) {
        AddressDTO address = addressService.findById(id);
        LOGGER.info("Received request to fetch address with ID: {}", id);
        return ResponseEntity.ok().body(address);
    }

    @PostMapping
    public ResponseEntity<AddressDTO> save(@Valid @RequestBody AddressDTO addressDTO) {
        UserResponseDTO userAuthenticated = userService.findUserAuthenticated();
        AddressDTO address = addressService.save(addressDTO, userAuthenticated);
        LOGGER.info("Received request to create a new address");
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(address);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressDTO> update(@Valid @RequestBody AddressDTO addressDTO, @PathVariable Long id) {
        UserResponseDTO userAuthenticated = userService.findUserAuthenticated();
        AddressDTO address = addressService.update(addressDTO, id, userAuthenticated);
        LOGGER.info("Received request to update an address with ID: {}", id);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(address);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AddressDTO> delete(@PathVariable Long id) {
        addressService.delete(id);
        LOGGER.info("Received request to delete an address with ID: {}", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).build();
    }
}
