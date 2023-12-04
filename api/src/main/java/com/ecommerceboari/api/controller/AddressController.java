package com.ecommerceboari.api.controller;

import com.ecommerceboari.api.dto.AddressDTO;
import com.ecommerceboari.api.dto.response.UserResponseDTO;
import com.ecommerceboari.api.service.AddressService;
import com.ecommerceboari.api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
