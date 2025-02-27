package com.example.ecommerce.repositories;

import com.example.ecommerce.models.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColorRepository extends JpaRepository<Color, Long> {
    Color findByName(String name);
    List<Color> findColorByName(String name);
}
