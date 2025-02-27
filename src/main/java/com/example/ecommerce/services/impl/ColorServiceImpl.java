package com.example.ecommerce.services.impl;

import com.example.ecommerce.models.Color;
import com.example.ecommerce.repositories.ColorRepository;
import com.example.ecommerce.services.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ColorServiceImpl implements ColorService {
    @Autowired
    private ColorRepository colorRepository;

    @Override
    public List<Color> getAllColors() {
        return colorRepository.findAll();
    }

    @Override
    public void deleteColor(long id) {
        colorRepository.deleteById(id);

    }

    @Override
    public void saveColor(Color color) {

        colorRepository.save(color);
    }

    @Override
    public Color getColorById(long id) {
        Optional< Color > optional = colorRepository.findById(id);
        Color color = null;
        if (optional.isPresent()) {
            color = optional.get();
        } else {
            throw new RuntimeException(" Color not found for id :: " + id);
        }
        return color;
    }
}
