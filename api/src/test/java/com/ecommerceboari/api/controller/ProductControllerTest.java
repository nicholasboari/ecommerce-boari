package com.ecommerceboari.api.controller;

import com.ecommerceboari.api.dto.ProductDTO;
import com.ecommerceboari.api.service.ProductService;
import com.ecommerceboari.api.util.ProductCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    @Test
    @DisplayName("Returns page of products inside page object when findAllPaged is called")
    void findAllPaged_ReturnsPageOfProducts_WhenSuccessful() {
        Pageable pageable = PageRequest.of(0, 3);
        Page<ProductDTO> productDTOPage = new PageImpl<>(Collections.singletonList(ProductCreator.createValidProductDTO()));

        Mockito.when(productService.findAllPaged(pageable)).thenReturn(productDTOPage);

        ResponseEntity<Page<ProductDTO>> responseEntity = productController.findAllPaged(pageable);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    @DisplayName("Returns a list of products when findAll is called")
    void findAll_ReturnsListOfProducts_WhenSuccessful() {
        List<ProductDTO> expectedProducts = Arrays.asList(
                ProductCreator.createValidProductDTO(),
                ProductCreator.createValidProductDTO(),
                ProductCreator.createValidProductDTO()
        );

        Mockito.when(productService.findAll()).thenReturn(expectedProducts);

        ResponseEntity<List<ProductDTO>> responseEntity = productController.findAll();

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isNotNull();
        Assertions.assertThat(responseEntity.getBody()).hasSize(expectedProducts.size());
    }

    @Test
    @DisplayName("Returns page of products by name inside page object when findAllPagedByName is called")
    void findAllPagedByName_ReturnsPageOfProducts_WhenSuccessful() {
        String productName = "SomeProduct";
        Pageable pageable = PageRequest.of(0, 3);
        Page<ProductDTO> productDTOPage = new PageImpl<>(Collections.singletonList(ProductCreator.createValidProductDTO()));

        Mockito.when(productService.findAllPagedByName(productName, pageable)).thenReturn(productDTOPage);

        ResponseEntity<Page<ProductDTO>> responseEntity = productController.findAllPagedByName(productName, pageable);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    @DisplayName("Returns page of products by category name inside page object when findAllPagedByCategoryName is called")
    void findAllPagedByCategoryName_ReturnsPageOfProducts_WhenSuccessful() {
        String categoryName = "SomeCategory";
        Pageable pageable = PageRequest.of(0, 3);
        Page<ProductDTO> productDTOPage = new PageImpl<>(Collections.singletonList(ProductCreator.createValidProductDTO()));

        Mockito.when(productService.findAllPagedByCategoryName(categoryName, pageable)).thenReturn(productDTOPage);

        ResponseEntity<Page<ProductDTO>> responseEntity = productController.findAllPagedByCategoryName(categoryName, pageable);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    @DisplayName("Returns page of products by brand name inside page object when findAllPagedByBrandName is called")
    void findAllPagedByBrandName_ReturnsPageOfProducts_WhenSuccessful() {
        String brandName = "SomeBrand";
        Pageable pageable = PageRequest.of(0, 3);
        Page<ProductDTO> productDTOPage = new PageImpl<>(Collections.singletonList(ProductCreator.createValidProductDTO()));

        Mockito.when(productService.findAllPagedByBrandName(brandName, pageable)).thenReturn(productDTOPage);

        ResponseEntity<Page<ProductDTO>> responseEntity = productController.findAllPagedByBrandName(brandName, pageable);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    @DisplayName("Creates a new product when save is called with a valid DTO")
    void save_CreatesNewProduct_WhenSuccessful() {
        ProductDTO validProductDTO = ProductCreator.createValidProductDTO();
        ProductDTO expectedProduct = ProductCreator.createValidProductDTO();

        Mockito.when(productService.save(validProductDTO)).thenReturn(expectedProduct);

        ResponseEntity<ProductDTO> responseEntity = productController.save(validProductDTO);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(responseEntity.getBody()).isNotNull();
        Assertions.assertThat(responseEntity.getBody().getId()).isEqualTo(expectedProduct.getId());
    }

    @Test
    @DisplayName("Deletes a product when delete is called with a valid ID")
    void delete_DeletesProduct_WhenSuccessful() {
        Long validId = 1L;

        ResponseEntity<Void> responseEntity = productController.delete(validId);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Mockito.verify(productService, Mockito.times(1)).delete(validId);
    }
}