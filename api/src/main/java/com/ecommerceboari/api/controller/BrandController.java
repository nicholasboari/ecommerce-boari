package com.ecommerceboari.api.controller;

import com.ecommerceboari.api.dto.BrandDTO;
import com.ecommerceboari.api.service.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/brands")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class BrandController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BrandController.class);
    private final BrandService brandService;

    @GetMapping
    public ResponseEntity<Page<BrandDTO>> findPaged(Pageable pageable) {
        Page<BrandDTO> paged = brandService.findPaged(pageable);
        LOGGER.info("Received request to fetch all brands paginated");
        return ResponseEntity.ok().body(paged);
    }

    @GetMapping("/all")
    public ResponseEntity<List<BrandDTO>> findAll() {
        List<BrandDTO> brandList = brandService.findAll();
        LOGGER.info("Received request to fetch all brands");
        return ResponseEntity.ok().body(brandList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandDTO> findById(@PathVariable Long id) {
        BrandDTO brand = brandService.findById(id);
        LOGGER.info("Received request to fetch brand with ID: {}", id);
        return ResponseEntity.ok().body(brand);
    }

    @GetMapping("/find")
    public ResponseEntity<List<BrandDTO>> findByName(@RequestParam String name) {
        List<BrandDTO> brand = brandService.findByNameContaining(name);
        LOGGER.info("Received request to fetch a brand by name");
        return ResponseEntity.ok().body(brand);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping
    public ResponseEntity<BrandDTO> save(@Valid @RequestBody BrandDTO brandDTO) {
        BrandDTO brand = brandService.save(brandDTO);
        LOGGER.info("Received request to create a new brand");
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(brand);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<BrandDTO> update(@Valid @RequestBody BrandDTO brandDTO, @PathVariable Long id){
        BrandDTO brandUpdated = brandService.update(brandDTO, id);
        LOGGER.info("Received request to update a brand with ID: {}", id);
        return ResponseEntity.ok().body(brandUpdated);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<BrandDTO>  save(@PathVariable Long id) {
        brandService.delete(id);
        LOGGER.info("Received request to delete a brand with ID: {}", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
