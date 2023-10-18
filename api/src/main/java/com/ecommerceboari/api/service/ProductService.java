package com.ecommerceboari.api.service;

import com.ecommerceboari.api.dto.BrandDTO;
import com.ecommerceboari.api.dto.CategoryDTO;
import com.ecommerceboari.api.dto.ProductDTO;
import com.ecommerceboari.api.exception.BadRequestException;
import com.ecommerceboari.api.model.Product;
import com.ecommerceboari.api.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;
    private final BrandService brandService;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    public List<ProductDTO> findAll() {
        return productRepository.findAll().stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();
    }

    public Page<ProductDTO> findAllPaged(Pageable pageable) {
        return productRepository.findAll(pageable).map(product -> modelMapper.map(product, ProductDTO.class));
    }

    public Page<ProductDTO> findAllPagedByName(String name, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCase(name, pageable).map(product -> modelMapper.map(product, ProductDTO.class));
    }

    public Page<ProductDTO> findAllPagedByCategoryName(String name, Pageable pageable) {
        return productRepository.findByCategoryNameContaining(name, pageable).map(product -> modelMapper.map(product, ProductDTO.class));
    }

    public Page<ProductDTO> findAllPagedByBrandName(String name, Pageable pageable) {
        return productRepository.findByBrandNameContaining(name, pageable).map(product -> modelMapper.map(product, ProductDTO.class));
    }

    public ProductDTO findById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new BadRequestException("Product not found!"));
        return modelMapper.map(product, ProductDTO.class);
    }

    @Transactional
    public ProductDTO save(ProductDTO productDTO) {
        // getting brand and category by ID
        Long brandId = productDTO.getBrand().getId();
        Long categoryId = productDTO.getCategory().getId();

        BrandDTO brandDTO = brandService.findById(brandId);
        CategoryDTO categoryDTO = categoryService.findById(categoryId);

        // setting brand and category
        productDTO.setBrand(brandDTO);
        productDTO.setCategory(categoryDTO);

        Product mapped = modelMapper.map(productDTO, Product.class);
        Product productSaved = productRepository.save(mapped);
        logger.info("Inserting {} into the database", productSaved);
        return modelMapper.map(productSaved, ProductDTO.class);
    }

    @Transactional
    public ProductDTO update(ProductDTO productDTO, Long id) {
        findById(id);
        ProductDTO build = ProductDTO.builder().build();

        BeanUtils.copyProperties(productDTO, build);
        build.setId(id);
        save(build);
        return build;
    }

    @Transactional
    public void delete(Long id) {
        productRepository.deleteById(id);
        logger.info("Object deleted from the database, ID: {}", id);
    }

}