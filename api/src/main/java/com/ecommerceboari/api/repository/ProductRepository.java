package com.ecommerceboari.api.repository;

import com.ecommerceboari.api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByBrandId(Long id);
}
