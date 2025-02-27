package com.example.ecommerce.repository;

import com.example.ecommerce.MobileEcommerceApplication;
import com.example.ecommerce.models.Brand;
import com.example.ecommerce.repositories.BrandRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(classes = MobileEcommerceApplication.class)
public class BrandRepositoryTest {
    @Autowired
    private BrandRepository brandRepository;

    @Test
    public void testFindById() {
        Brand brand = new Brand();
        brand.setId(2);
        brand.setName("Samsung");
        Brand result = brandRepository.findById(brand.getId()).get();
        assertEquals(brand.getName(), result.getName());
    }

    @Test
    public void testSave() {
        Brand brand = new Brand();
            brand.setName("Oppo");
        brandRepository.save(brand);
        Brand found = brandRepository.findByName(brand.getName());
        assertEquals(brand.getId(), found.getId());
    }
}
