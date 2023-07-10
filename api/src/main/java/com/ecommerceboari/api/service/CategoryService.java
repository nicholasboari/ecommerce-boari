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
        return categoryRepository.findAll().stream().map(category -> new CategoryDTO(
                        category.getId(),
                        category.getName(),
                        category.getImageUrl()))
                .toList();
    }

    public Page<CategoryDTO> findPaged(Pageable pageable) {
        Page<Category> categories = categoryRepository.findAll(pageable);
        return categories.map(category -> new CategoryDTO(category.getId(), category.getName(), category.getImageUrl()));
    }

    public CategoryDTO findById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new BadRequestException("Category not found"));
        return new CategoryDTO(category.getId(), category.getName(), category.getImageUrl());
    }

    @Transactional
    public CategoryDTO save(CategoryDTO categoryDTO) {
        Category existingCategory = categoryRepository.findByName(categoryDTO.name());
        if (existingCategory != null) {
            throw new DataIntegrityViolationException("Category name already exists!");
        }

        Category category = Category.builder()
                .id(categoryDTO.id())
                .name(categoryDTO.name())
                .imageUrl(categoryDTO.imageUrl())
                .build();

        Category categorySaved = categoryRepository.save(category);
        return new CategoryDTO(categorySaved.getId(), categorySaved.getName(), category.getImageUrl());
    }

    @Transactional
    public CategoryDTO update(CategoryDTO categoryDTO, Long id) {
        CategoryDTO dto = findById(id);
        Category category = Category.builder()
                .id(dto.id())
                .name(categoryDTO.name())
                .imageUrl(categoryDTO.imageUrl())
                .build();

        CategoryDTO categorySaved = new CategoryDTO(category.getId(), category.getName(), category.getImageUrl());
        return save(categorySaved);
    }

    @Transactional
    public void delete(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new BadRequestException("Category not found"));
        categoryRepository.delete(category);
    }

}
