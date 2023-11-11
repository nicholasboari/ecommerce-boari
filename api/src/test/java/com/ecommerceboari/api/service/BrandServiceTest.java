package com.ecommerceboari.api.service;

import com.ecommerceboari.api.dto.BrandDTO;
import com.ecommerceboari.api.model.Brand;
import com.ecommerceboari.api.repository.BrandRepository;
import com.ecommerceboari.api.util.BrandCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class BrandServiceTest {

    @InjectMocks
    private BrandService brandService;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private ModelMapper modelMapper;

    private List<Brand> brandList;
    private Brand brand;

    @BeforeEach
    void setup() {
        brandList = List.of(BrandCreator.createValidBrand());
        brand = BrandCreator.createValidBrand();

        Mockito.when(modelMapper.map(Mockito.any(Brand.class), Mockito.eq(BrandDTO.class))).thenReturn(BrandCreator.createValidBrandDTO());
        Mockito.when(modelMapper.map(Mockito.any(BrandDTO.class), Mockito.eq(Brand.class))).thenReturn(BrandCreator.createValidBrand());
    }

    @Test
    @DisplayName("Return a page of brands when successful")
    void findPaged_ShouldReturnAPageOfBrands_WhenSuccessful() {
        PageImpl<Brand> brandPage = new PageImpl<>(Collections.singletonList(BrandCreator.createValidBrand()));
        Pageable pageable = PageRequest.of(0, 1);

        Mockito.when(brandRepository.findAll(pageable)).thenReturn(brandPage);

        Page<BrandDTO> result = brandService.findPaged(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("Return a list of brands when successful")
    void findAll_ShouldReturnAListOfBrands_WhenSuccessful() {
        Mockito.when(brandRepository.findAll()).thenReturn(brandList);

        List<BrandDTO> result = brandService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Return a brand when ID exists")
    void findById_ShouldReturnBrand_WhenSuccessful() {
        Mockito.when(brandRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(brand));

        BrandDTO result = brandService.findById(1L);

        assertNotNull(result);
        assertEquals(brand.getId(), result.getId());
        assertEquals(brand.getName(), result.getName());
    }

    @Test
    @DisplayName("Save a new brand when ID is null")
    void save_ShouldCreateBrand_WhenSuccessful() {
        Mockito.when(brandRepository.findByName(Mockito.anyString())).thenReturn(Collections.emptyList());
        Mockito.when(brandRepository.save(Mockito.any(Brand.class))).thenReturn(BrandCreator.createValidBrand());

        BrandDTO createdBrand = brandService.save(BrandCreator.createValidBrandDTO());

        assertNotNull(createdBrand);
        assertEquals("Brand A", createdBrand.getName());
    }

    @Test
    @DisplayName("Update a brand when ID exists")
    void update_ShouldUpdateBrand_WhenIdExists() {
        Long brandId = 1L;
        Mockito.when(brandRepository.findById(brandId)).thenReturn(Optional.of(brand));
        Mockito.when(brandRepository.save(Mockito.any(Brand.class))).thenReturn(brand);

        BrandDTO updatedBrand = brandService.update(BrandCreator.createValidBrandDTO(), brandId);

        assertNotNull(updatedBrand);
        assertEquals(brandId, updatedBrand.getId());
    }

    @Test
    @DisplayName("Delete a brand when ID exists")
    void delete_ShouldDeleteBrand_WhenIdExists() {
        Long brandId = 1L;
        Mockito.when(brandRepository.findById(brandId)).thenReturn(Optional.of(brand));
        Mockito.doNothing().when(brandRepository).deleteById(brandId);

        brandService.delete(brandId);

        Mockito.verify(brandRepository, Mockito.times(1)).deleteById(brandId);
    }
}