package com.ecommerceboari.api.service;

import com.ecommerceboari.api.dto.BrandDTO;
import com.ecommerceboari.api.dto.CategoryDTO;
import com.ecommerceboari.api.dto.ProductDTO;
import com.ecommerceboari.api.exception.BadRequestException;
import com.ecommerceboari.api.model.Product;
import com.ecommerceboari.api.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final BrandService brandService;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    public List<ProductDTO> findAll() {
        return productRepository.findAll().stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();
    }

    public ProductDTO findById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new BadRequestException("Product not found!"));
        return modelMapper.map(product, ProductDTO.class);
    }

    public ProductDTO save(ProductDTO productDTO){
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
        return modelMapper.map(productSaved, ProductDTO.class);
    }

}
