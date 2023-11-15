package com.ecommerceboari.api.controller;

import com.ecommerceboari.api.dto.CategoryDTO;
import com.ecommerceboari.api.service.CategoryService;
import com.ecommerceboari.api.util.CategoryCreator;
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
class CategoryControllerTest {

    @InjectMocks
    private CategoryController categoryController;

    @Mock
    private CategoryService categoryService;

    @Test
    @DisplayName("Returns page of categories inside page object when findPaged is called")
    void findPaged_ReturnsPageOfCategories_WhenSuccessful() {
        Pageable pageable = PageRequest.of(0, 3);
        Page<CategoryDTO> categoryDTOPage = new PageImpl<>(Collections.singletonList(CategoryCreator.createValidCategoryDTO()));

        Mockito.when(categoryService.findPaged(pageable)).thenReturn(categoryDTOPage);

        ResponseEntity<Page<CategoryDTO>> responseEntity = categoryController.findPaged(pageable);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    @DisplayName("Returns a list of categories when findAll is called")
    void findAll_ReturnsListOfCategories_WhenSuccessful() {
        List<CategoryDTO> expectedCategories = Arrays.asList(
                CategoryCreator.createValidCategoryDTO(),
                CategoryCreator.createValidCategoryDTO(),
                CategoryCreator.createValidCategoryDTO()
        );

        Mockito.when(categoryService.findAll()).thenReturn(expectedCategories);

        ResponseEntity<List<CategoryDTO>> responseEntity = categoryController.findAll();

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isNotNull();
        Assertions.assertThat(responseEntity.getBody()).hasSize(expectedCategories.size());
    }

    @Test
    @DisplayName("Returns a category when findById is called with a valid ID")
    void findById_ReturnsCategory_WhenSuccessful() {
        Long validId = 1L;
        CategoryDTO expectedCategory = CategoryCreator.createValidCategoryDTO();

        Mockito.when(categoryService.findById(validId)).thenReturn(expectedCategory);

        ResponseEntity<CategoryDTO> responseEntity = categoryController.findById(validId);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isNotNull();
        Assertions.assertThat(responseEntity.getBody().getId()).isEqualTo(validId);
    }

    @Test
    @DisplayName("Returns a list of categories when findByNameContaining is called with a valid name")
    void findByNameContaining_ReturnsListOfCategories_WhenSuccessful() {
        String validName = "SomeCategory";
        List<CategoryDTO> expectedCategories = Arrays.asList(
                CategoryCreator.createValidCategoryDTO(),
                CategoryCreator.createValidCategoryDTO()
        );

        Mockito.when(categoryService.findByNameContaining(validName)).thenReturn(expectedCategories);

        ResponseEntity<List<CategoryDTO>> responseEntity = categoryController.findByNameContaining(validName);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isNotNull();
        Assertions.assertThat(responseEntity.getBody()).hasSize(expectedCategories.size());
    }

    @Test
    @DisplayName("Creates a new category when save is called with a valid DTO")
    void save_CreatesNewCategory_WhenSuccessful() {
        CategoryDTO validCategoryDTO = CategoryCreator.createValidCategoryDTO();
        CategoryDTO expectedCategory = CategoryCreator.createValidCategoryDTO();

        Mockito.when(categoryService.save(validCategoryDTO)).thenReturn(expectedCategory);

        ResponseEntity<CategoryDTO> responseEntity = categoryController.save(validCategoryDTO);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(responseEntity.getBody()).isNotNull();
        Assertions.assertThat(responseEntity.getBody().getId()).isEqualTo(expectedCategory.getId());
    }

    @Test
    @DisplayName("Updates a category when update is called with a valid DTO and ID")
    void update_UpdatesCategory_WhenSuccessful() {
        Long validId = 1L;
        CategoryDTO validCategoryDTO = CategoryCreator.createValidCategoryDTO();
        CategoryDTO expectedCategory = CategoryCreator.createValidCategoryDTO();

        Mockito.when(categoryService.update(validCategoryDTO, validId)).thenReturn(expectedCategory);

        ResponseEntity<CategoryDTO> responseEntity = categoryController.update(validCategoryDTO, validId);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isNotNull();
        Assertions.assertThat(responseEntity.getBody().getId()).isEqualTo(expectedCategory.getId());
    }

    @Test
    @DisplayName("Deletes a category when delete is called with a valid ID")
    void delete_DeletesCategory_WhenSuccessful() {
        Long validId = 1L;

        ResponseEntity<CategoryDTO> responseEntity = categoryController.delete(validId);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(responseEntity.getBody()).isNull();
        Mockito.verify(categoryService, Mockito.times(1)).delete(validId);
    }
}