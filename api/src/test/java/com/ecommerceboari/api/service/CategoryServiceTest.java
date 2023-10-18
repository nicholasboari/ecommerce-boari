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
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setName("Category A");

        List<Category> existingCategories = Collections.singletonList(new Category(1L, "Category A"));

        // create a list of category
        List<Category> categoryList = List.of(
                new Category(1L, "Category A"),
                new Category(2L, "Category B"),
                new Category(3L, "Category C"));

        // create a page of category
        PageImpl<Category> categoryPage = new PageImpl<>(List.of(CategoryCreator.createValidCategory()));

        Mockito.when(categoryRepository.findAll()).thenReturn(categoryList);
        Mockito.when(categoryRepository.findAll(ArgumentMatchers.any(PageRequest.class))).thenReturn(categoryPage);
        Mockito.when(categoryRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(CategoryCreator.createValidCategory()));
        Mockito.when(categoryRepository.findByNameContaining(ArgumentMatchers.anyString())).thenReturn(categoryList);
        Mockito.when(modelMapper.map(Mockito.any(Category.class), Mockito.eq(CategoryDTO.class)))
                .thenReturn(categoryDTO);
        Mockito.when(categoryRepository.findByName("New Category")).thenReturn(Collections.emptyList());
        Mockito.when(modelMapper.map(Mockito.any(CategoryDTO.class), Mockito.eq(Category.class))).thenReturn(CategoryCreator.createValidCategory());
        Mockito.when(categoryRepository.findByName("Category A")).thenReturn(existingCategories);
    }

    @Test
    @DisplayName("return a page of category when successful")
    void findAll_ShouldReturnAPageOfCategory_WhenSuccessful() {
        Page<CategoryDTO> categoryPage = categoryService.findPaged(PageRequest.of(1, 1));
        String expectedName = CategoryCreator.createValidCategory().getName();
        Long expectedId = CategoryCreator.createValidCategory().getId();

        Assertions.assertNotNull(categoryPage);
        Assertions.assertEquals(1L, categoryPage.toList().size());
        Assertions.assertEquals(expectedId, categoryPage.toList().get(0).getId());
        Assertions.assertEquals(expectedName, categoryPage.get().toList().get(0).getName());
    }

    @Test
    @DisplayName("return a list of categories when successful")
    void findAll_ShouldReturnAListOfCategories_WhenSuccessful() {
        List<CategoryDTO> categoryList = categoryService.findAll();
        String expectedName = CategoryCreator.createValidCategory().getName();
        Long expectedId = CategoryCreator.createValidCategory().getId();

        Assertions.assertNotNull(categoryList);
        Assertions.assertEquals(3L, categoryList.size());
        Assertions.assertEquals(expectedId, categoryList.get(0).getId());
        Assertions.assertEquals(expectedName, categoryList.get(0).getName());
    }

    @Test
    @DisplayName("return a category when successful")
    void findById_ShouldReturnCategory_WhenSuccessful() {
        CategoryDTO category = categoryService.findById(1L);
        String expectedName = category.getName();
        Long expectedId = category.getId();

        Assertions.assertNotNull(category);
        Assertions.assertEquals(expectedId, category.getId());
        Assertions.assertEquals(expectedName, category.getName());
    }

    @Test
    @DisplayName("return a list of categories when successful")
    void findByNameContaining_ShouldReturnListOfCategories_WhenSuccessful() {
        String categoryName = "Category A";
        List<CategoryDTO> categoryList = categoryService.findByNameContaining(categoryName);

        Assertions.assertNotNull(categoryList);
        Assertions.assertFalse(categoryList.isEmpty());
        for (CategoryDTO category : categoryList) {
            Long expectedId = CategoryCreator.createValidCategory().getId();
            String expectedName = CategoryCreator.createValidCategory().getName();

            Assertions.assertEquals(expectedId, category.getId());
            Assertions.assertEquals(expectedName, category.getName());
        }
    }

    @Test
    @DisplayName("Save a new category when name is unique")
    void save_ShouldSaveNewCategory_WhenNameIsUnique() {
        CategoryDTO categoryDTO = new CategoryDTO();
        Category category = new Category();
        category.setName("Category A");

        Mockito.when(modelMapper.map(categoryDTO, Category.class)).thenReturn(category);
        Mockito.when(categoryRepository.save(category)).thenReturn(CategoryCreator.createValidCategory());

        CategoryDTO savedCategory = categoryService.save(categoryDTO);

        Assertions.assertNotNull(savedCategory);
        Assertions.assertEquals(category.getName(), savedCategory.getName());
    }

    @Test
    @DisplayName("Throw DataIntegrityViolationException when category name already exists")
    void save_ShouldThrowDataIntegrityViolationException_WhenCategoryNameAlreadyExists() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("Category A");


        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            categoryService.save(categoryDTO);
        });
    }
}