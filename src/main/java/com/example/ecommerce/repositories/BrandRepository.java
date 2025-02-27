package com.example.ecommerce.repositories;

import com.example.ecommerce.models.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository  extends JpaRepository<Brand, Long> {
    List<Brand> findBrandByName(String name);

    Brand findByName(String name);
}
