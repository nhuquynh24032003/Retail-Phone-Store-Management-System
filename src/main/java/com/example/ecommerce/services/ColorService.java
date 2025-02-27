package com.example.ecommerce.services;

import com.example.ecommerce.models.Color;

import java.util.List;

public interface ColorService {
    List<Color> getAllColors();
    void deleteColor(long id);
    void saveColor(Color color);
    Color getColorById(long id);
}
