package com.ecommerceboari.api.s3;

import com.ecommerceboari.api.auth.AuthenticationService;
import com.ecommerceboari.api.dto.ProductDTO;
import com.ecommerceboari.api.service.ProductService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Service
@Data
@RequiredArgsConstructor
public class S3ProductService {

    private final S3Client s3;
    private final ProductService productService;
    private final AuthenticationService authenticationService;

    @Value("${aws.s3.buckets.customer}")
    private String customer;
    private static final String PATH_FOLDER = "image-product/%s";

    public String putS3Object(Long productId, MultipartFile file) {
        var product = productService.findById(productId);
        deleteS3Object(productId);

        try {
            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(customer)
                    .key(PATH_FOLDER.formatted(product.getName() + productId))
                    .contentType(file.getContentType())
                    .build();

            s3.putObject(objectRequest, RequestBody.fromBytes(file.getBytes()));
            GetUrlRequest urlRequest = GetUrlRequest.builder()
                    .bucket(customer)
                    .key(PATH_FOLDER.formatted(productId))
                    .build();

            String url = s3.utilities().getUrl(urlRequest).toString();
            product.setImageUrl(url);

            productService.update(product, productId);
            return url;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] getS3Object(Long productId) throws IOException {
        var product = productService.findById(productId);

        GetObjectRequest build = GetObjectRequest.builder()
                .bucket(customer)
                .key(PATH_FOLDER.formatted(product.getName() + productId))
                .build();
        return s3.getObject(build).readAllBytes();
    }

    public void deleteS3Object(Long productId) {
        ProductDTO product = productService.findById(productId);

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(customer)
                .key(PATH_FOLDER.formatted(product.getName() + productId))
                .build();

        product.setImageUrl(null);

        productService.update(product, productId);
        s3.deleteObject(deleteObjectRequest);
    }
}