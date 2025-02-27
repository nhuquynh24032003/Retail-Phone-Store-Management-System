package com.example.ecommerce.repository;

import com.example.ecommerce.MobileEcommerceApplication;
import com.example.ecommerce.models.Color;
import com.example.ecommerce.repositories.ColorRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(classes = MobileEcommerceApplication.class)
public class ColorRepositoryTest {
    @Autowired
    private ColorRepository colorRepository;

    @Test
    public void testFindById() {
        Color color = new Color();
        color.setId(3);
        color.setName("Black");
        Color result = colorRepository.findById(color.getId()).get();
        assertEquals(color.getName(), result.getName());
    }

    @Test
    public void testSave() {
        Color color = new Color();
            color.setName("Yellow");
        colorRepository.save(color);
        Color found = colorRepository.findByName(color.getName());
        assertEquals(color.getId(), found.getId());
    }
}
