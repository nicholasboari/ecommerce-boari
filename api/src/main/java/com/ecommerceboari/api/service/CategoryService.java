package com.ecommerceboari.api.service;

import com.ecommerceboari.api.dto.CategoryDTO;
import com.ecommerceboari.api.exception.BadRequestException;
import com.ecommerceboari.api.model.Category;
import com.ecommerceboari.api.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryDTO> findAll() {
        return categoryRepository.findAll().stream().map(CategoryService::buildDto).toList();
    }

    public Page<CategoryDTO> findPaged(Pageable pageable) {
        Page<Category> categories = categoryRepository.findAll(pageable);
        return categories.map(CategoryService::buildDto);
    }

    public CategoryDTO findById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new BadRequestException("Category not found"));
        return buildDto(category);
    }

    public List<CategoryDTO> findByNameContaining(String name) {
        List<Category> categories = categoryRepository.findByNameContaining(name);
        return categories.stream().map(CategoryService::buildDto).toList();
    }


    @Transactional
    public CategoryDTO save(CategoryDTO categoryDTO) {
        List<Category> list = categoryRepository.findByName(categoryDTO.getName());
        if (!list.isEmpty()) {
            throw new DataIntegrityViolationException("Category name already exists!");
        }

        Category category = buildModel(categoryDTO);
        Category categorySaved = categoryRepository.save(category);
        return buildDto(categorySaved);
    }

    @Transactional
    public CategoryDTO update(CategoryDTO categoryDTO, Long id) {
        findById(id);
        Category category = buildModel(categoryDTO);
        category.setId(id);
        CategoryDTO categorySaved = buildDto(category);
        return save(categorySaved);
    }

    @Transactional
    public void delete(Long id) {
        CategoryDTO category = findById(id);
        categoryRepository.deleteById(category.getId());
    }

    public static Category buildModel(CategoryDTO categoryDTO){
        return Category.builder()
                .id(categoryDTO.getId())
                .name(categoryDTO.getName())
                .imageUrl(categoryDTO.getImageUrl())
                .build();
    }

    public static CategoryDTO buildDto(Category category){
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .imageUrl(category.getImageUrl())
                .build();
    }

}
