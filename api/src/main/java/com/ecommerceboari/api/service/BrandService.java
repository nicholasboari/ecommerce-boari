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
        return brandRepository.findAll().stream().map(brand -> modelMapper.map(brand, BrandDTO.class)).toList();
    }

    public Page<BrandDTO> findPaged(Pageable pageable) {
        Page<Brand> brands = brandRepository.findAll(pageable);
        return brands.map(brand -> modelMapper.map(brand, BrandDTO.class));
    }

    public BrandDTO findById(Long id) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> new BadRequestException("Brand not found"));
        return modelMapper.map(brand, BrandDTO.class);
    }

    public List<BrandDTO> findByNameContaining(String name) {
        List<Brand> brands = brandRepository.findByNameContaining(name);
        return brands.stream().map(brand -> modelMapper.map(brand, BrandDTO.class)).toList();
    }

    @Transactional
    public BrandDTO save(BrandDTO brandDTO) {
        List<Brand> list = brandRepository.findByName(brandDTO.getName());
        if (!list.isEmpty()) {
            throw new DataIntegrityViolationException("Brand name already exists!");
        }

        Brand brand = modelMapper.map(brandDTO, Brand.class);
        Brand brandSaved = brandRepository.save(brand);
        return modelMapper.map(brandSaved, BrandDTO.class);
    }

    @Transactional
    public BrandDTO update(BrandDTO brandDTO, Long id) {
        findById(id);
        Brand brand =  modelMapper.map(brandDTO, Brand.class);
        brand.setId(id);
        BrandDTO brandSaved =  modelMapper.map(brand, BrandDTO.class);
        return save(brandSaved);
    }

    @Transactional
    public void delete(Long id) {
        BrandDTO brand = findById(id);
        brandRepository.deleteById(brand.getId());
    }

}
