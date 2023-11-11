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

    private List<Category> categoryList;
    private PageImpl<Category> categoryPage;
    private Category category;
    private CategoryDTO categoryDTO;

    @BeforeEach
    public void setup() {
        categoryDTO = CategoryCreator.createValidCategoryDTO();
        category = CategoryCreator.createValidCategory();

        // create a list of category
        categoryList = List.of(CategoryCreator.createValidCategory());

        // create a page of category
        categoryPage = new PageImpl<>(List.of(CategoryCreator.createValidCategory()));

        Mockito.when(categoryRepository.findByName("New Category")).thenReturn(Collections.emptyList());
        Mockito.when(modelMapper.map(Mockito.any(Category.class), Mockito.eq(CategoryDTO.class))).thenReturn(categoryDTO);
        Mockito.when(modelMapper.map(Mockito.any(CategoryDTO.class), Mockito.eq(Category.class))).thenReturn(CategoryCreator.createValidCategory());
    }

    @Test
    @DisplayName("Return a page of categories when successful")
    void findAll_ShouldReturnAPageOfCategories_WhenSuccessful() {
        Mockito.when(categoryRepository.findAll(ArgumentMatchers.any(PageRequest.class))).thenReturn(categoryPage);

        Page<CategoryDTO> categoryPage = categoryService.findPaged(PageRequest.of(1, 1));
        String expectedName = CategoryCreator.createValidCategory().getName();
        Long expectedId = CategoryCreator.createValidCategory().getId();

        Assertions.assertNotNull(categoryPage);
        Assertions.assertEquals(1L, categoryPage.toList().size());
        Assertions.assertEquals(expectedId, categoryPage.toList().get(0).getId());
        Assertions.assertEquals(expectedName, categoryPage.get().toList().get(0).getName());
    }

    @Test
    @DisplayName("Return a list of categories when successful")
    void findAll_ShouldReturnAListOfCategories_WhenSuccessful() {
        Mockito.when(categoryRepository.findAll()).thenReturn(categoryList);

        List<CategoryDTO> categoryList = categoryService.findAll();
        String expectedName = CategoryCreator.createValidCategory().getName();
        Long expectedId = CategoryCreator.createValidCategory().getId();

        Assertions.assertNotNull(categoryList);
        Assertions.assertEquals(1L, categoryList.size());
        Assertions.assertEquals(expectedId, categoryList.get(0).getId());
        Assertions.assertEquals(expectedName, categoryList.get(0).getName());
    }

    @Test
    @DisplayName("Return a category when ID exists")
    void findById_ShouldReturnCategory_WhenSuccessful() {
        Mockito.when(categoryRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(CategoryCreator.createValidCategory()));

        CategoryDTO category = categoryService.findById(1L);
        String expectedName = category.getName();
        Long expectedId = category.getId();

        Assertions.assertNotNull(category);
        Assertions.assertEquals(expectedId, category.getId());
        Assertions.assertEquals(expectedName, category.getName());
    }

    @Test
    @DisplayName("Return a list of categories when name containing")
    void findByNameContaining_ShouldReturnListOfCategories_WhenSuccessful() {
        Mockito.when(categoryRepository.findByNameContaining(ArgumentMatchers.anyString())).thenReturn(categoryList);

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
        Mockito.when(categoryRepository.save(category)).thenReturn(CategoryCreator.createValidCategory());
        Mockito.when(modelMapper.map(categoryDTO, Category.class)).thenReturn(category);

        CategoryDTO savedCategory = categoryService.save(categoryDTO);

        Assertions.assertNotNull(savedCategory);
        Assertions.assertEquals(category.getName(), savedCategory.getName());
    }

    @Test
    @DisplayName("Throw DataIntegrityViolationException when category name already exists")
    void save_ShouldThrowDataIntegrityViolationException_WhenCategoryNameAlreadyExists() {
        List<Category> existingCategories = Collections.singletonList(new Category(1L, "Category A"));
        Mockito.when(categoryRepository.findByName("Category A")).thenReturn(existingCategories);

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("Category A");

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            categoryService.save(categoryDTO);
        });
    }
}