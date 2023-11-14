package com.ecommerceboari.api.repository;

import com.ecommerceboari.api.model.Category;
import com.ecommerceboari.api.util.CategoryCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @DisplayName("Save creates category when Successful")
    @Test
    void save_PersistCategory_WhenSuccessful() {
        Category categoryToBeSaved = CategoryCreator.createValidCategory();

        Category categorySaved = this.categoryRepository.save(categoryToBeSaved);

        Assertions.assertThat(categorySaved).isNotNull();

        Assertions.assertThat(categorySaved.getId()).isNotNull();

        Assertions.assertThat(categorySaved.getName()).isEqualTo(categoryToBeSaved.getName());
    }

    @DisplayName("Save updates category when Successful")
    @Test
    void update_ReplaceCategory_WhenSuccessful() {
        Category categoryToBeSaved = CategoryCreator.createValidCategory();

        Category categorySaved = this.categoryRepository.save(categoryToBeSaved);

        categorySaved.setName("UpdatedCategory");

        Category categoryUpdated = this.categoryRepository.save(categorySaved);

        Assertions.assertThat(categoryUpdated).isNotNull();

        Assertions.assertThat(categoryUpdated.getId()).isNotNull();

        Assertions.assertThat(categoryUpdated.getName()).isEqualTo(categorySaved.getName());
    }

    @DisplayName("Delete removes category when Successful")
    @Test
    void delete_RemovesCategory_WhenSuccessful() {
        Category categoryToBeSaved = CategoryCreator.createValidCategory();

        Category categorySaved = this.categoryRepository.save(categoryToBeSaved);

        this.categoryRepository.delete(categorySaved);

        Optional<Category> categoryOptional = this.categoryRepository.findById(categorySaved.getId());

        Assertions.assertThat(categoryOptional).isNotPresent();
    }

    @DisplayName("Find by name containing returns list of categories when Successful")
    @Test
    void findByNameContaining_ReturnsListOfCategories_WhenSuccessful() {
        Category categoryToBeSaved = CategoryCreator.createValidCategory();
        this.categoryRepository.save(categoryToBeSaved);

        List<Category> categories = this.categoryRepository.findByNameContaining(categoryToBeSaved.getName().substring(0, 3));

        Assertions.assertThat(categories).isNotEmpty().contains(categoryToBeSaved);
    }

    @DisplayName("Find by name returns list of categories when Successful")
    @Test
    void findByName_ReturnsListOfCategories_WhenSuccessful() {
        Category categoryToBeSaved = CategoryCreator.createValidCategory();
        this.categoryRepository.save(categoryToBeSaved);

        List<Category> categories = this.categoryRepository.findByName(categoryToBeSaved.getName());

        Assertions.assertThat(categories).isNotEmpty().contains(categoryToBeSaved);
    }
}