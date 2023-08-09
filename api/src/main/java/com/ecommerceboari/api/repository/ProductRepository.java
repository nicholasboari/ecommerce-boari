package com.ecommerceboari.api.repository;

import com.ecommerceboari.api.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByBrandId(Long id);

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Product> findByCategoryNameContaining(String name, Pageable pageable);

    Page<Product> findByBrandNameContaining(String name, Pageable pageable);
}
