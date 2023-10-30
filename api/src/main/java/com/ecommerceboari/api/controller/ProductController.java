package com.ecommerceboari.api.controller;

import com.ecommerceboari.api.dto.ProductDTO;
import com.ecommerceboari.api.service.ProductService;
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
@RequestMapping("/api/v1/products")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> findAllPaged(Pageable pageable){
        Page<ProductDTO> products = productService.findAllPaged(pageable);
        LOGGER.info("Received request to fetch all products paginated");
        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductDTO>> findAll(){
        List<ProductDTO> products = productService.findAll();
        LOGGER.info("Received request to fetch all products");
        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/{name}")
    public ResponseEntity<Page<ProductDTO>> findAllPagedByName(@PathVariable String name, Pageable pageable){
        Page<ProductDTO> products = productService.findAllPagedByName(name, pageable);
        LOGGER.info("Request received to search for a product by name");
        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/category/{name}")
    public ResponseEntity<Page<ProductDTO>> findAllPagedByCategoryName(@PathVariable String name, Pageable pageable){
        Page<ProductDTO> products = productService.findAllPagedByCategoryName(name, pageable);
        LOGGER.info("Request received to search for a product by category name");
        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/brand/{name}")
    public ResponseEntity<Page<ProductDTO>> findAllPagedByBrandName(@PathVariable String name, Pageable pageable){
        Page<ProductDTO> products = productService.findAllPagedByBrandName(name, pageable);
        LOGGER.info("Request received to search for a product by brand name");
        return ResponseEntity.ok().body(products);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping
    public ResponseEntity<ProductDTO> save(@RequestBody ProductDTO productDTO){
        ProductDTO product = productService.save(productDTO);
        LOGGER.info("Received request to create a new product");
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(product);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        productService.delete(id);
        LOGGER.info("Received request to update a new product");
        return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).build();
    }
}
