package com.ecommerceboari.api.service;

import com.ecommerceboari.api.dto.ProductDTO;
import com.ecommerceboari.api.model.Product;
import com.ecommerceboari.api.repository.ProductRepository;
import com.ecommerceboari.api.util.BrandCreator;
import com.ecommerceboari.api.util.CategoryCreator;
import com.ecommerceboari.api.util.ProductCreator;
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
    private ModelMapper modelMapper;

    @Mock
    private BrandService brandService;

    @Mock
    private CategoryService categoryService;

    private List<ProductDTO> productDTOList;
    private Page<Product> productPage;
    private ProductDTO productDTO;
    private Product product = new Product();
    private List<Product> productList;

    @BeforeEach
    public void setup() {
        productDTO = ProductCreator.createValidProductDTO();

        // create a list of product
        productList = List.of(ProductCreator.createValidProduct());

        // create a page of product
        productPage = new PageImpl<>(List.of(ProductCreator.createValidProduct()));

        productDTOList = productList.stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();

        Mockito.when(modelMapper.map(Mockito.any(Product.class), Mockito.eq(ProductDTO.class))).thenReturn(productDTO);
        Mockito.when(modelMapper.map(Mockito.any(ProductDTO.class), Mockito.eq(Product.class))).thenReturn(product);
    }

    @Test
    @DisplayName("Return a list of products when successful")
    void findAll_ShouldReturnListOfProducts_WhenSuccessful() {
        Mockito.when(productRepository.findAll()).thenReturn(productList);

        List<ProductDTO> result = productService.findAll();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(productDTOList.size(), result.size());
    }

    @Test
    @DisplayName("Return a page of products when successful")
    void findAllPaged_ShouldReturnPageOfProducts_WhenSuccessful() {
        Mockito.when(productRepository.findAll(ArgumentMatchers.any(Pageable.class))).thenReturn(productPage);

        Page<ProductDTO> result = productService.findAllPaged(PageRequest.of(0, 10));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(productPage.getTotalElements(), result.getTotalElements());
    }

    @Test
    @DisplayName("Return a page of products by name when successful")
    void findAllPagedByName_ShouldReturnPageOfProductsByName_WhenSuccessful() {
        Mockito.when(productRepository.findByNameContainingIgnoreCase(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(Pageable.class))).thenReturn(productPage);

        Page<ProductDTO> result = productService.findAllPagedByName("Product A", PageRequest.of(0, 10));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(productPage.getTotalElements(), result.getTotalElements());
    }

    @Test
    @DisplayName("Return a page of products by category name when successful")
    void findAllPagedByCategoryName_ShouldReturnPageOfProductsByCategoryName_WhenSuccessful() {
        Mockito.when(productRepository.findByCategoryNameContaining(ArgumentMatchers.anyString(), ArgumentMatchers.any(Pageable.class))).thenReturn(productPage);
        Page<ProductDTO> result = productService.findAllPagedByCategoryName("Category A", PageRequest.of(0, 10));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(productPage.getTotalElements(), result.getTotalElements());
    }

    @Test
    @DisplayName("Return a product when ID exists")
    void findById_ShouldReturnCategory_WhenSuccessful() {
        Mockito.when(productRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(ProductCreator.createValidProduct()));

        ProductDTO product = productService.findById(1L);
        String expectedName = product.getName();
        Long expectedId = product.getId();

        Assertions.assertNotNull(product);
        Assertions.assertEquals(expectedId, product.getId());
        Assertions.assertEquals(expectedName, product.getName());
    }

    @Test
    @DisplayName("Save a new product when ID null")
    void save_ShouldSaveNewProduct_WhenIdIsNull() {
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(ProductCreator.createValidProduct());
        Mockito.when(brandService.findById(Mockito.anyLong())).thenReturn(BrandCreator.createValidBrandDTO());
        Mockito.when(categoryService.findById(Mockito.anyLong())).thenReturn(CategoryCreator.createValidCategoryDTO());

        ProductDTO savedProduct = productService.save(productDTO);

        Assertions.assertNotNull(savedProduct);
        Assertions.assertEquals("Iphone X", savedProduct.getName());
    }

    @Test
    @DisplayName("Delete a product when ID exists")
    void delete_ShouldDeleteProduct_WhenIdExists() {
        Long productId = 1L;
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        Mockito.doNothing().when(productRepository).deleteById(productId);

        productService.delete(productId);
        Mockito.verify(productRepository, Mockito.times(1)).deleteById(productId);
    }
}