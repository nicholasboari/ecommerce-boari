package com.ecommerceboari.api.controller;

import com.ecommerceboari.api.dto.ProductDTO;
import com.ecommerceboari.api.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    //TODO ADMIN

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> findAll(){
        List<ProductDTO> products = productService.findAll();
        return ResponseEntity.ok().body(products);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> save(@RequestBody ProductDTO productDTO){
        ProductDTO product = productService.save(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(product);
    }
}
