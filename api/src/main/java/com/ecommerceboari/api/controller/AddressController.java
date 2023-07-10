package com.ecommerceboari.api.controller;

import com.ecommerceboari.api.dto.AddressDTO;
import com.ecommerceboari.api.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<Page<AddressDTO>> findPaged(Pageable pageable) {
        Page<AddressDTO> paged = addressService.findPaged(pageable);
        return ResponseEntity.ok().body(paged);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AddressDTO>> findAll(){
        List<AddressDTO> addressList = addressService.findAll();
        return ResponseEntity.ok().body(addressList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDTO> findById(@PathVariable Long id){
        AddressDTO address = addressService.findById(id);
        return ResponseEntity.ok().body(address);
    }

    @PostMapping
    public ResponseEntity<AddressDTO> save(@Valid @RequestBody AddressDTO addressDTO) {
        AddressDTO address = addressService.save(addressDTO);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(address);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressDTO> update(@Valid @RequestBody AddressDTO addressDTO, @PathVariable Long id) {
        AddressDTO address = addressService.update(addressDTO, id);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(address);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AddressDTO> delete(@PathVariable Long id) {
        addressService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).build();
    }
}
