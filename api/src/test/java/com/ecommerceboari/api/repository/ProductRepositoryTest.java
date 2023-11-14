package com.ecommerceboari.api.repository;

import com.ecommerceboari.api.model.Brand;
import com.ecommerceboari.api.model.Product;
import com.ecommerceboari.api.util.BrandCreator;
import com.ecommerceboari.api.util.CategoryCreator;
import com.ecommerceboari.api.util.ProductCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setup(){
        brandRepository.save(BrandCreator.createValidBrand());
        categoryRepository.save(CategoryCreator.createValidCategory());
    }

    @DisplayName("Save creates product when Successful")
    @Test
    void save_PersistProduct_WhenSuccessful() {
        Product productToBeSaved = ProductCreator.createValidProduct();

        Product productSaved = this.productRepository.save(productToBeSaved);

        Assertions.assertThat(productSaved).isNotNull();
        Assertions.assertThat(productSaved.getId()).isNotNull();

        Assertions.assertThat(productSaved.getBrand().getId()).isNotNull();
        Assertions.assertThat(productSaved.getCategory().getId()).isNotNull();
    }


    @DisplayName("Update updates product when Successful")
    @Test
    void update_ReplaceProduct_WhenSuccessful() {
        Product productToBeSaved = ProductCreator.createValidProduct();

        Product productSaved = this.productRepository.save(productToBeSaved);

        productSaved.setName("UpdatedProduct");

        Product productUpdated = this.productRepository.save(productSaved);

        Assertions.assertThat(productUpdated).isNotNull();
        Assertions.assertThat(productUpdated.getId()).isNotNull();
        Assertions.assertThat(productUpdated.getName()).isEqualTo(productSaved.getName());
    }

    @DisplayName("Delete removes product when Successful")
    @Test
    void delete_RemovesProduct_WhenSuccessful() {
        Product productToBeSaved = ProductCreator.createValidProduct();

        Product productSaved = this.productRepository.save(productToBeSaved);

        this.productRepository.delete(productSaved);

        Optional<Product> productOptional = this.productRepository.findById(productSaved.getId());

        Assertions.assertThat(productOptional).isNotPresent();
    }

    @DisplayName("Find by brand ID returns product when Successful")
    @Test
    void findByBrandId_ReturnsProduct_WhenSuccessful() {
        Product productToBeSaved = ProductCreator.createValidProduct();
        Brand brand = productToBeSaved.getBrand();
        this.productRepository.save(productToBeSaved);

        Optional<Product> foundProduct = this.productRepository.findByBrandId(brand.getId());

        Assertions.assertThat(foundProduct).isPresent();
        Assertions.assertThat(foundProduct.get().getName()).isEqualTo(productToBeSaved.getName());
    }

    @DisplayName("Find by name containing (ignore case) returns page of products when Successful")
    @Test
    void findByNameContainingIgnoreCase_ReturnsPageOfProducts_WhenSuccessful() {
        Product productToBeSaved = ProductCreator.createValidProduct();
        this.productRepository.save(productToBeSaved);

        Page<Product> productsPage = this.productRepository.findByNameContainingIgnoreCase(
                productToBeSaved.getName().substring(0, 3), PageRequest.of(0, 10));

        Assertions.assertThat(productsPage).isNotEmpty();
        Assertions.assertThat(productsPage.getContent()).hasSize(1);
    }

    @DisplayName("Find by category name containing returns page of products when Successful")
    @Test
    void findByCategoryNameContaining_ReturnsPageOfProducts_WhenSuccessful() {
        Product productToBeSaved = ProductCreator.createValidProduct();
        this.productRepository.save(productToBeSaved);


        Page<Product> productsPage = this.productRepository.findByCategoryNameContaining(
                productToBeSaved.getCategory().getName().substring(0, 3), PageRequest.of(0, 10));

        Assertions.assertThat(productsPage).isNotEmpty();
        Assertions.assertThat(productsPage.getContent()).hasSize(1);
    }

    @DisplayName("Find by brand name containing returns page of products when Successful")
    @Test
    void findByBrandNameContaining_ReturnsPageOfProducts_WhenSuccessful() {
        Product productToBeSaved = ProductCreator.createValidProduct();
        this.productRepository.save(productToBeSaved);

        Page<Product> productsPage = this.productRepository.findByBrandNameContaining(
                productToBeSaved.getBrand().getName().substring(0, 3), PageRequest.of(0, 10));

        Assertions.assertThat(productsPage).isNotEmpty();
        Assertions.assertThat(productsPage.getContent()).hasSize(1);
    }
}
