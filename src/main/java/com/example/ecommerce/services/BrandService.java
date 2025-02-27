package com.example.ecommerce.services;

import com.example.ecommerce.models.Brand;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BrandService {
    List<Brand> getAll();
    void deleteBrandByID(long id);
    void saveBrand(Brand brand);
    Brand getBrandById(long id);

}
