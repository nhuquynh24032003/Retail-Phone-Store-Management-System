package com.example.ecommerce.services.impl;

import com.example.ecommerce.models.Brand;
import com.example.ecommerce.repositories.BrandRepository;
import com.example.ecommerce.services.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private final BrandRepository brandRepository;

    public BrandServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public List<Brand> getAll() {
        return brandRepository.findAll();
    }

    @Override
    public void deleteBrandByID(long id) { brandRepository.deleteById(id); }

    @Override
    public void saveBrand(Brand brand) { brandRepository.save(brand); }

    @Override
    public Brand getBrandById(long id) {
        Optional< Brand > optional = brandRepository.findById(id);
        Brand brand = null;
        if (optional.isPresent()) {
            brand = optional.get();
        } else {
            throw new RuntimeException(" Brand not found for id :: " + id);
        }
        return brand;
    }
}
