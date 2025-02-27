package com.example.ecommerce.services.impl;

import com.example.ecommerce.models.Capacity;
import com.example.ecommerce.repositories.CapacityRepository;
import com.example.ecommerce.services.CapacityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
@Service
public class CapacityServiceImpl implements CapacityService {
    @Autowired
    private CapacityRepository capacityRepository;
    @Override
    public List<Capacity> getAllCapaCiTy() {

        return capacityRepository.findAll();
    }
    @Override
    public void deleteCapacity(long id) {
        capacityRepository.deleteById(id);
    }
    @Override
    public void saveCapcity(Capacity capacity) {
        this.capacityRepository.save(capacity);

    }


    @Override
    public Capacity getCapacityById(long id) {
        Optional< Capacity > optional = capacityRepository.findById(id);
        Capacity capacity = null;
        if (optional.isPresent()) {
            capacity = optional.get();
        } else {
            throw new RuntimeException(" Brand not found for id :: " + id);
        }
        return capacity;
    }
}
