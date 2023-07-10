package com.ecommerceboari.api.controller;

import com.ecommerceboari.api.dto.CategoryDTO;
import com.ecommerceboari.api.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Page<CategoryDTO>> findPaged(Pageable pageable) {
        Page<CategoryDTO> paged = categoryService.findPaged(pageable);
        return ResponseEntity.ok().body(paged);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CategoryDTO>> findAll() {
        List<CategoryDTO> categoryList = categoryService.findAll();
        return ResponseEntity.ok().body(categoryList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> findById(@PathVariable Long id) {
        CategoryDTO category = categoryService.findById(id);
        return ResponseEntity.ok().body(category);
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> save(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO category = categoryService.save(categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> update(@RequestBody CategoryDTO categoryDTO, @PathVariable Long id){
        CategoryDTO categoryUpdated = categoryService.update(categoryDTO, id);
        return ResponseEntity.ok().body(categoryUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryDTO> save(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
