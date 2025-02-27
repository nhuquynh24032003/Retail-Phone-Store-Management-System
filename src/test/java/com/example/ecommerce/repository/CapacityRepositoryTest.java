package com.example.ecommerce.repository;

import com.example.ecommerce.MobileEcommerceApplication;
import com.example.ecommerce.models.Capacity;
import com.example.ecommerce.repositories.CapacityRepository;
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
public class CapacityRepositoryTest {
    @Autowired
    private CapacityRepository capacityRepository;

    @Test
    public void testFindById() {
        Capacity capacity = new Capacity();
        capacity.setId(4);
        capacity.setSizeInGB(128);
        Capacity result = capacityRepository.findById(capacity.getId()).get();
        assertEquals(capacity.getSizeInGB(), result.getSizeInGB());
    }

    @Test
    public void testSave() {
        Capacity capacity = new Capacity();
            capacity.setSizeInGB(1024);
        capacityRepository.save(capacity);
        Capacity found = capacityRepository.findCapacityBySizeInGB(capacity.getSizeInGB());
        assertEquals(capacity.getId(), found.getId());
    }
}
