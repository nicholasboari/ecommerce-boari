package com.ecommerceboari.api.controller;

import com.ecommerceboari.api.dto.BrandDTO;
import com.ecommerceboari.api.service.BrandService;
import com.ecommerceboari.api.util.BrandCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
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
class BrandControllerTest {

    @InjectMocks
    private BrandController brandController;

    @Mock
    private BrandService brandService;

    private Pageable pageable;

    private final List<BrandDTO> expectedBrands = Arrays.asList(
            BrandCreator.createValidBrandDTO(),
            BrandCreator.createValidBrandDTO(),
            BrandCreator.createValidBrandDTO()
    );

    @BeforeEach
    void setup(){
        pageable = PageRequest.of(0, 3);

        Page<BrandDTO> brandDTOPage = new PageImpl<>(Collections.singletonList(BrandCreator.createValidBrandDTO()));

        Mockito.when(this.brandService.findPaged(ArgumentMatchers.any(Pageable.class))).thenReturn(brandDTOPage);
    }

    @Test
    @DisplayName("Return a page of brand inside page object when successful")
    void findAllPaged_ReturnsPageOfBrand_WhenSuccessful(){
        String expectedName = BrandCreator.createValidBrandDTO().getName();

        ResponseEntity<Page<BrandDTO>> responseEntity = brandController.findAllPaged(pageable);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        Page<BrandDTO> brandPage = responseEntity.getBody();

        Assertions.assertThat(brandPage).isNotNull();
        Assertions.assertThat(brandPage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("Returns a list of brands when findAll is called")
    void findAll_ReturnsListOfBrands_WhenSuccessful() {
        Mockito.when(brandService.findAll()).thenReturn(expectedBrands);

        ResponseEntity<List<BrandDTO>> responseEntity = brandController.findAll();

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        Assertions.assertThat(responseEntity.getBody()).isNotNull();
        Assertions.assertThat(responseEntity.getBody()).hasSize(expectedBrands.size());
    }

    @Test
    @DisplayName("Return a brand when findById is called with a valid ID")
    void findById_ReturnsBrand_WhenSuccessful() {
        Long validId = 1L;
        BrandDTO expectedBrand = BrandCreator.createValidBrandDTO();

        Mockito.when(this.brandService.findById(validId)).thenReturn(expectedBrand);

        ResponseEntity<BrandDTO> responseEntity = brandController.findById(validId);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isNotNull();
        Assertions.assertThat(responseEntity.getBody().getId()).isEqualTo(validId);
    }

    @Test
    @DisplayName("Return a list of brands when findByName is called with a valid name")
    void findByName_ReturnsListOfBrands_WhenSuccessful() {
        String validName = "SomeBrand";

        Mockito.when(this.brandService.findByNameContaining(validName)).thenReturn(expectedBrands);

        ResponseEntity<List<BrandDTO>> responseEntity = brandController.findByName(validName);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isNotNull();
        Assertions.assertThat(responseEntity.getBody()).hasSize(expectedBrands.size());
    }

    @Test
    @DisplayName("Create a new brand when save is called with a valid DTO")
    void save_CreatesNewBrand_WhenSuccessful() {
        BrandDTO validBrandDTO = BrandCreator.createValidBrandDTO();
        BrandDTO expectedBrand = BrandCreator.createValidBrandDTO();

        Mockito.when(this.brandService.save(validBrandDTO)).thenReturn(expectedBrand);

        ResponseEntity<BrandDTO> responseEntity = brandController.save(validBrandDTO);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(responseEntity.getBody()).isNotNull();
        Assertions.assertThat(responseEntity.getBody().getId()).isEqualTo(expectedBrand.getId());
    }

    @Test
    @DisplayName("Update a brand when update is called with a valid DTO and ID")
    void update_UpdatesBrand_WhenSuccessful() {
        Long validId = 1L;
        BrandDTO validBrandDTO = BrandCreator.createValidBrandDTO();
        BrandDTO expectedBrand = BrandCreator.createValidBrandDTO();

        Mockito.when(this.brandService.update(validBrandDTO, validId)).thenReturn(expectedBrand);

        ResponseEntity<BrandDTO> responseEntity = brandController.update(validBrandDTO, validId);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isNotNull();
        Assertions.assertThat(responseEntity.getBody().getId()).isEqualTo(expectedBrand.getId());
    }

    @Test
    @DisplayName("Delete a brand when delete is called with a valid ID")
    void delete_DeletesBrand_WhenSuccessful() {
        Long validId = 1L;

        ResponseEntity<BrandDTO> responseEntity = brandController.delete(validId);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(responseEntity.getBody()).isNull();
        Mockito.verify(this.brandService, Mockito.times(1)).delete(validId);
    }
}