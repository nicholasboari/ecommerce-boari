package com.ecommerceboari.api.repository;

import com.ecommerceboari.api.model.Brand;
import com.ecommerceboari.api.util.BrandCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
class BrandRepositoryTest {

    @Autowired
    private BrandRepository brandRepository;

    @DisplayName("Save creates brand when Successful")
    @Test
    void save_PersistBrand_WhenSuccessful() {
        Brand brandToBeSaved = BrandCreator.createValidBrand();

        Brand brandSaved = this.brandRepository.save(brandToBeSaved);

        Assertions.assertThat(brandSaved).isNotNull();

        Assertions.assertThat(brandSaved.getId()).isNotNull();

        Assertions.assertThat(brandSaved.getName()).isEqualTo(brandToBeSaved.getName());
    }

    @DisplayName("Save updates brand when Successful")
    @Test
    void update_ReplaceBrand_WhenSuccessful() {
        Brand brandToBeSaved = BrandCreator.createValidBrand();

        Brand brandSaved = this.brandRepository.save(brandToBeSaved);

        brandSaved.setName("UpdatedBrand");

        Brand brandUpdated = this.brandRepository.save(brandSaved);

        Assertions.assertThat(brandUpdated).isNotNull();

        Assertions.assertThat(brandUpdated.getId()).isNotNull();

        Assertions.assertThat(brandUpdated.getName()).isEqualTo(brandSaved.getName());
    }

    @DisplayName("Delete removes brand when Successful")
    @Test
    void delete_RemovesBrand_WhenSuccessful() {
        Brand brandToBeSaved = BrandCreator.createValidBrand();

        Brand brandSaved = this.brandRepository.save(brandToBeSaved);

        this.brandRepository.delete(brandSaved);

        Optional<Brand> brandOptional = this.brandRepository.findById(brandSaved.getId());

        Assertions.assertThat(brandOptional).isNotPresent();
    }

    @DisplayName("Find by name containing returns list of brands when Successful")
    @Test
    void findByNameContaining_ReturnsListOfBrands_WhenSuccessful() {
        Brand brandToBeSaved = BrandCreator.createValidBrand();
        this.brandRepository.save(brandToBeSaved);

        List<Brand> brands = this.brandRepository.findByNameContaining(brandToBeSaved.getName().substring(0, 3));

        Assertions.assertThat(brands).contains(brandToBeSaved).isNotEmpty();
    }

    @DisplayName("Find by name returns list of brands when Successful")
    @Test
    void findByName_ReturnsListOfBrands_WhenSuccessful() {
        Brand brandToBeSaved = BrandCreator.createValidBrand();
        this.brandRepository.save(brandToBeSaved);

        List<Brand> brands = this.brandRepository.findByName(brandToBeSaved.getName());

        Assertions.assertThat(brands).isNotEmpty().contains(brandToBeSaved);
    }
}