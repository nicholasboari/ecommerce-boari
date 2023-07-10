package com.ecommerceboari.api.repository;

import com.ecommerceboari.api.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface BrandRepository extends JpaRepository<Brand, Long> {

    List<Brand> findByNameContaining(String name);

    List<Brand> findByName(String name);
}
