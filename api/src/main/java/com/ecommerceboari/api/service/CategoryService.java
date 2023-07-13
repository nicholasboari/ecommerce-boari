package com.ecommerceboari.api.service;

import com.ecommerceboari.api.dto.CategoryDTO;
import com.ecommerceboari.api.exception.BadRequestException;
import com.ecommerceboari.api.model.Category;
import com.ecommerceboari.api.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    public List<CategoryDTO> findAll() {
        return categoryRepository.findAll().stream().map(category -> modelMapper.map(category, CategoryDTO.class)).toList();
    }

    public Page<CategoryDTO> findPaged(Pageable pageable) {
        Page<Category> categories = categoryRepository.findAll(pageable);
        return categories.map(category -> modelMapper.map(category, CategoryDTO.class));
    }

    public CategoryDTO findById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new BadRequestException("Category not found"));
        return modelMapper.map(category, CategoryDTO.class);
    }

    public List<CategoryDTO> findByNameContaining(String name) {
        List<Category> categories = categoryRepository.findByNameContaining(name);
        return categories.stream().map(category -> modelMapper.map(category, CategoryDTO.class)).toList();
    }


    @Transactional
    public CategoryDTO save(CategoryDTO categoryDTO) {
        List<Category> list = categoryRepository.findByName(categoryDTO.getName());
        if (!list.isEmpty()) {
            throw new DataIntegrityViolationException("Category name already exists!");
        }

        Category category = modelMapper.map(categoryDTO, Category.class);
        Category categorySaved = categoryRepository.save(category);
        return modelMapper.map(categorySaved, CategoryDTO.class);
    }

    @Transactional
    public CategoryDTO update(CategoryDTO categoryDTO, Long id) {
        findById(id);
        Category category = modelMapper.map(categoryDTO, Category.class);

        category.setId(id);
        CategoryDTO categorySaved = modelMapper.map(category, CategoryDTO.class);
        return save(categorySaved);
    }

    @Transactional
    public void delete(Long id) {
        CategoryDTO category = findById(id);
        categoryRepository.deleteById(category.getId());
    }

}
