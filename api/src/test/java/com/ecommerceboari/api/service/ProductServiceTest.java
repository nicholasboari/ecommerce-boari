package com.ecommerceboari.api.service;

import com.ecommerceboari.api.dto.ProductDTO;
import com.ecommerceboari.api.model.Product;
import com.ecommerceboari.api.repository.ProductRepository;
import com.ecommerceboari.api.util.BrandCreator;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BrandService brandService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private ModelMapper modelMapper;

    List<ProductDTO> productDTOList;
    Page<Product> productPage;
    ProductDTO productDTO = new ProductDTO();
    Product product = new Product();

    @BeforeEach
    public void setup() {
        productDTO.setName("Product A");
        productDTO.setName("New Product");
        productDTO.setBrand(BrandCreator.createValidBrandDTO());
        productDTO.setCategory(CategoryCreator.createValidCategoryDTO());

        productPage = new PageImpl<>(List.of(
                new Product(1L, "Product A", 100L, "image1.png", BrandCreator.createValidBrand(), CategoryCreator.createValidCategory())
        ));

        List<Product> productList = List.of(
                new Product(1L, "Product A", 100L, "image1.png", BrandCreator.createValidBrand(), CategoryCreator.createValidCategory()),
                new Product(2L, "Product B", 200L, "image2.png", BrandCreator.createValidBrand(), CategoryCreator.createValidCategory()),
                new Product(3L, "Product C", 300L, "image3png", BrandCreator.createValidBrand(), CategoryCreator.createValidCategory())
        );

        productDTOList = productList.stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();

        Mockito.when(productRepository.findAll()).thenReturn(productList);
        Mockito.when(modelMapper.map(Mockito.any(), Mockito.eq(ProductDTO.class))).thenReturn(productDTO);
        Mockito.when(productRepository.findAll(ArgumentMatchers.any(Pageable.class))).thenReturn(productPage);
        Mockito.when(productRepository.findByNameContainingIgnoreCase(ArgumentMatchers.anyString(), ArgumentMatchers.any(Pageable.class))).thenReturn(productPage);
        Mockito.when(productRepository.findByCategoryNameContaining(ArgumentMatchers.anyString(), ArgumentMatchers.any(Pageable.class))).thenReturn(productPage);
        Mockito.when(modelMapper.map(productDTO, Product.class)).thenReturn(product);
        Mockito.when(brandService.findById(Mockito.anyLong())).thenReturn(BrandCreator.createValidBrandDTO());
        Mockito.when(categoryService.findById(Mockito.anyLong())).thenReturn(CategoryCreator.createValidCategoryDTO());
        Mockito.when(productRepository.save(product)).thenReturn(product);
    }

    @Test
    @DisplayName("Return a list of products when findAll is called")
    void findAll_ShouldReturnListOfProducts() {
        List<ProductDTO> result = productService.findAll();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(productDTOList.size(), result.size());
    }

    @Test
    @DisplayName("Return a page of products when findAllPaged is called")
    void findAllPaged_ShouldReturnPageOfProducts() {
        Page<ProductDTO> result = productService.findAllPaged(PageRequest.of(0, 10));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(productPage.getTotalElements(), result.getTotalElements());
    }

    @Test
    @DisplayName("Return a page of products by name when findAllPagedByName is called")
    void findAllPagedByName_ShouldReturnPageOfProductsByName() {
        Page<ProductDTO> result = productService.findAllPagedByName("Product A", PageRequest.of(0, 10));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(productPage.getTotalElements(), result.getTotalElements());
    }

    @Test
    @DisplayName("Return a page of products by category name when findAllPagedByCategoryName is called")
    void findAllPagedByCategoryName_ShouldReturnPageOfProductsByCategoryName() {
        Page<ProductDTO> result = productService.findAllPagedByCategoryName("Category A", PageRequest.of(0, 10));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(productPage.getTotalElements(), result.getTotalElements());
    }

    @Test
    @DisplayName("Save a new product when save is called")
    void save_ShouldSaveNewProduct() {
        ProductDTO savedProduct = productService.save(productDTO);

        Assertions.assertNotNull(savedProduct);
        Assertions.assertEquals("New Product", savedProduct.getName());
    }

    @Test
    @DisplayName("Delete a product when delete is called")
    void delete_ShouldDeleteProduct() {
        Long productId = 1L;
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        Mockito.doNothing().when(productRepository).deleteById(productId);

        productService.delete(productId);
        Mockito.verify(productRepository, Mockito.times(1)).deleteById(productId);
    }
}