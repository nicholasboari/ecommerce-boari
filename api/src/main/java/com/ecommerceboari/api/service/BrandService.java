package com.ecommerceboari.api.service;

import com.ecommerceboari.api.dto.BrandDTO;
import com.ecommerceboari.api.exception.BadRequestException;
import com.ecommerceboari.api.model.Brand;
import com.ecommerceboari.api.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;

    public List<BrandDTO> findAll() {
        return brandRepository.findAll().stream().map(brand -> new BrandDTO(
                        brand.getId(),
                        brand.getName()))
                .toList();
    }

    public Page<BrandDTO> findPaged(Pageable pageable) {
        Page<Brand> brands = brandRepository.findAll(pageable);
        return brands.map(brand -> new BrandDTO(brand.getId(), brand.getName()));
    }

    public BrandDTO findById(Long id) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> new BadRequestException("Brand not found"));
        return new BrandDTO(brand.getId(), brand.getName());
    }

    public List<BrandDTO> findByNameContaining(String name) {
        List<Brand> brands = brandRepository.findByNameContaining(name);
        return brands
                .stream()
                .map(brand -> new BrandDTO(
                        brand.getId(),
                        brand.getName())).toList();
    }

    @Transactional
    public BrandDTO save(BrandDTO brandDTO) {
        List<Brand> list = brandRepository.findByName(brandDTO.name());
        if (!list.isEmpty()) {
            throw new DataIntegrityViolationException("Brand name already exists!");
        }

        Brand brand = Brand.builder()
                .id(brandDTO.id())
                .name(brandDTO.name())
                .build();
        Brand brandSaved = brandRepository.save(brand);
        return new BrandDTO(brandSaved.getId(), brandSaved.getName());
    }

    @Transactional
    public BrandDTO update(BrandDTO brandDTO, Long id) {
        BrandDTO dto = findById(id);
        Brand brand = Brand.builder()
                .id(dto.id())
                .name(brandDTO.name())
                .build();

        BrandDTO brandSaved = new BrandDTO(brand.getId(), brand.getName());
        return save(brandSaved);
    }

    @Transactional
    public void delete(Long id) {
        BrandDTO brand = findById(id);
        brandRepository.deleteById(brand.id());
    }

}
