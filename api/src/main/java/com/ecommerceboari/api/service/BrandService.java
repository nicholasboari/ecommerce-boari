package com.ecommerceboari.api.service;

import com.ecommerceboari.api.dto.BrandDTO;
import com.ecommerceboari.api.exception.BadRequestException;
import com.ecommerceboari.api.model.Brand;
import com.ecommerceboari.api.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    public List<BrandDTO> findAll() {
        return brandRepository.findAll().stream().map(BrandService::buildDto).toList();
    }

    public Page<BrandDTO> findPaged(Pageable pageable) {
        Page<Brand> brands = brandRepository.findAll(pageable);
        return brands.map(BrandService::buildDto);
    }

    public BrandDTO findById(Long id) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> new BadRequestException("Brand not found"));
        return buildDto(brand);
    }

    public List<BrandDTO> findByNameContaining(String name) {
        List<Brand> brands = brandRepository.findByNameContaining(name);
        return brands.stream().map(BrandService::buildDto).toList();
    }

    @Transactional
    public BrandDTO save(BrandDTO brandDTO) {
        List<Brand> list = brandRepository.findByName(brandDTO.getName());
        if (!list.isEmpty()) {
            throw new DataIntegrityViolationException("Brand name already exists!");
        }

        Brand brand = buildModel(brandDTO);
        Brand brandSaved = brandRepository.save(brand);
        return buildDto(brandSaved);
    }

    @Transactional
    public BrandDTO update(BrandDTO brandDTO, Long id) {
        findById(id);
        Brand brand = buildModel(brandDTO);
        brand.setId(id);
        BrandDTO brandSaved = buildDto(brand);
        return save(brandSaved);
    }

    @Transactional
    public void delete(Long id) {
        BrandDTO brand = findById(id);
        brandRepository.deleteById(brand.getId());
    }

    public static Brand buildModel(BrandDTO brandDTO){
        return Brand.builder()
                .id(brandDTO.getId())
                .name(brandDTO.getName())
                .build();
    }

    public static BrandDTO buildDto(Brand brand){
        return BrandDTO.builder()
                .id(brand.getId())
                .name(brand.getName())
                .build();
    }
}
