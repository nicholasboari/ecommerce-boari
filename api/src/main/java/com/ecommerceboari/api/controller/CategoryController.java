package com.ecommerceboari.api.controller;

import com.ecommerceboari.api.dto.CategoryDTO;
import com.ecommerceboari.api.service.CategoryService;
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
@RequestMapping("/api/v1/categories")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class CategoryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Page<CategoryDTO>> findPaged(Pageable pageable) {
        Page<CategoryDTO> paged = categoryService.findPaged(pageable);
        LOGGER.info("Received request to fetch all categories paginated");
        return ResponseEntity.ok().body(paged);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CategoryDTO>> findAll() {
        List<CategoryDTO> categoryList = categoryService.findAll();
        LOGGER.info("Received request to fetch all categories");
        return ResponseEntity.ok().body(categoryList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> findById(@PathVariable Long id) {
        CategoryDTO category = categoryService.findById(id);
        LOGGER.info("Received request to fetch a category with ID: {}", id);
        return ResponseEntity.ok().body(category);
    }

    @GetMapping("/find")
    public ResponseEntity<List<CategoryDTO>> findByNameContaining(@RequestParam String name) {
        List<CategoryDTO> category = categoryService.findByNameContaining(name);
        LOGGER.info("Received request to fetch a category by name");
        return ResponseEntity.ok().body(category);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping
    public ResponseEntity<CategoryDTO> save(@Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO category = categoryService.save(categoryDTO);
        LOGGER.info("Received request to create a new category");
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(category);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> update(@Valid @RequestBody CategoryDTO categoryDTO, @PathVariable Long id) {
        CategoryDTO categoryUpdated = categoryService.update(categoryDTO, id);
        LOGGER.info("Received request to update a category with ID: {}", id);
        return ResponseEntity.ok().body(categoryUpdated);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryDTO> save(@PathVariable Long id) {
        categoryService.delete(id);
        LOGGER.info("Received request to delete a category with ID: {}", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
