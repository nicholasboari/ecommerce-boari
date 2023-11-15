package com.ecommerceboari.api.controller.image;

import com.ecommerceboari.api.s3.S3ProductService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@ExtendWith(SpringExtension.class)
class ProductImageControllerTest {

    @InjectMocks
    private ProductImageController productImageController;

    @Mock
    private S3ProductService productService;

    @Test
    @DisplayName("Returns product image as byte array when getProductImage is called")
    void getProductImage_ReturnsProductImage_WhenSuccessful() throws IOException {
        Long productId = 1L;
        byte[] imageBytes = new byte[]{};

        Mockito.when(productService.getS3Object(productId)).thenReturn(imageBytes);

        ResponseEntity<byte[]> responseEntity = productImageController.getProductImage(productId);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo(imageBytes);
        Assertions.assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.IMAGE_JPEG);
    }

    @Test
    @DisplayName("Returns URL when uploadProductImage is called")
    void uploadProductImage_ReturnsImageUrl_WhenSuccessful() {
        byte[] imageBytes = new byte[]{};
        Long productId = 1L;
        MultipartFile file = new MockMultipartFile("test", imageBytes);

        String imageUrl = "https://example.com/image.jpg";

        Mockito.when(productService.putS3Object(productId, file)).thenReturn(imageUrl);

        ResponseEntity<String> responseEntity = productImageController.uploadProductImage(productId, file);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo(imageUrl);
    }

    @Test
    @DisplayName("Deletes product image when deleteProductImage is called")
    void deleteProductImage_DeletesProductImage_WhenSuccessful() {
        Long productId = 1L;

        ResponseEntity<Void> responseEntity = productImageController.deleteProductImage(productId);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Mockito.verify(productService, Mockito.times(1)).deleteS3Object(productId);
    }

}