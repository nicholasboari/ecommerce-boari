package com.ecommerceboari.api.controller;

import com.ecommerceboari.api.dto.ProductDTO;
import com.ecommerceboari.api.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<List<ProductDTO>> findAll(){
        List<ProductDTO> products = productService.findAll();
        return ResponseEntity.ok().body(products);
    }

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> findAllPaged(Pageable pageable){
        Page<ProductDTO> products = productService.findAllPaged(pageable);
        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/{name}")
    public ResponseEntity<Page<ProductDTO>> findAllPagedByName(@PathVariable String name, Pageable pageable){
        Page<ProductDTO> products = productService.findAllPagedByName(name, pageable);
        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/category/{name}")
    public ResponseEntity<Page<ProductDTO>> findAllPagedByCategoryName(@PathVariable String name, Pageable pageable){
        Page<ProductDTO> products = productService.findAllPagedByCategoryName(name, pageable);
        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/brand/{name}")
    public ResponseEntity<Page<ProductDTO>> findAllPagedByBrandName(@PathVariable String name, Pageable pageable){
        Page<ProductDTO> products = productService.findAllPagedByBrandName(name, pageable);
        return ResponseEntity.ok().body(products);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping
    public ResponseEntity<ProductDTO> save(@RequestBody ProductDTO productDTO){
        ProductDTO product = productService.save(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(product);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        productService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).build();
    }
}
