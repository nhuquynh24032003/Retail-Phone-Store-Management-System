package com.example.ecommerce.services;

import com.example.ecommerce.models.Capacity;

import java.util.List;

public interface CapacityService {

    List<Capacity> getAllCapaCiTy();
    void deleteCapacity(long id);
    void saveCapcity(Capacity capacity);
    Capacity getCapacityById(long id);
}
