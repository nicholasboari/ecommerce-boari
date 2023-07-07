package com.ecommerceboari.api.service;

import com.ecommerceboari.api.dto.CategoryDTO;
import com.ecommerceboari.api.model.Category;
import com.ecommerceboari.api.repository.CategoryRepository;
import com.ecommerceboari.api.util.CategoryCreator;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void setup() {

        // create a list of category
        List<Category> categoryList = List.of(
                new Category(1L, "Category A", "image1.png"),
                new Category(2L, "Category B", "image2.png"),
                new Category(3L, "Category C", "image3.png"));

        // create a page of category
        PageImpl<Category> categoryPage = new PageImpl<>(List.of(CategoryCreator.createValidCategory()));

        Mockito.when(categoryRepository.findAll()).thenReturn(categoryList);
        Mockito.when(categoryRepository.findAll(ArgumentMatchers.any(PageRequest.class))).thenReturn(categoryPage);
        Mockito.when(categoryRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(CategoryCreator.createValidCategory()));
    }

    @Test
    @DisplayName("return a page of category when successful")
    void findAll_ShouldReturnAPageOfCategory_WhenSuccessful() {
        Page<CategoryDTO> categoryPage = categoryService.findPaged(PageRequest.of(1, 1));
        String expectedName = CategoryCreator.createValidCategory().getName();
        Long expectedId = CategoryCreator.createValidCategory().getId();

        Assertions.assertNotNull(categoryPage);
        Assertions.assertEquals(1L, categoryPage.toList().size());
        Assertions.assertEquals(expectedId, categoryPage.toList().get(0).id());
        Assertions.assertEquals(expectedName, categoryPage.get().toList().get(0).name());
    }

    @Test
    @DisplayName("return a list of category when successful")
    void findAll_ShouldReturnAListOfCategory_WhenSuccessful() {
        List<CategoryDTO> categoryList = categoryService.findAll();
        String expectedName = CategoryCreator.createValidCategory().getName();
        Long expectedId = CategoryCreator.createValidCategory().getId();

        Assertions.assertNotNull(categoryList);
        Assertions.assertEquals(3L, categoryList.size());
        Assertions.assertEquals(expectedId, categoryList.get(0).id());
        Assertions.assertEquals(expectedName, categoryList.get(0).name());
    }

    @Test
    @DisplayName("return a category when successful")
    void findById_ShouldReturnCategory_WhenSuccessful() {
        CategoryDTO category = categoryService.findById(1L);
        String expectedName = category.name();
        Long expectedId = category.id();

        Assertions.assertNotNull(category);
        Assertions.assertEquals(expectedId, category.id());
        Assertions.assertEquals(expectedName, category.name());
    }

}