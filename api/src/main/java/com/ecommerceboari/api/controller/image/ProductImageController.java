package com.ecommerceboari.api.controller.image;

import com.ecommerceboari.api.s3.S3ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/products/image")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProductImageController {

    private final S3ProductService productService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @GetMapping("/{productId}")
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long productId) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(productService.getS3Object(productId), headers, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping("/{productId}")
    public ResponseEntity<String> uploadProductImage(@PathVariable Long productId, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(productService.putS3Object(productId, file));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProductImage(@PathVariable Long productId){
        productService.deleteS3Object(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
