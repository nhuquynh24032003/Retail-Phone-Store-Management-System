package com.example.ecommerce.services.impl;

import com.example.ecommerce.models.Specification;
import com.example.ecommerce.repositories.SpecificationRepository;
import com.example.ecommerce.services.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpecificationServiceImpl implements SpecificationService {

    @Autowired
    private SpecificationRepository specificationRepository;
    @Override
    public List < Specification > getAllSpecification() {

        return specificationRepository.findAll();
    }
    @Override
    public void deleteSpecification(long id) {
        specificationRepository.deleteById(id);

    }
    @Override
    public void saveSpecification(Specification specification) {
        specificationRepository.save(specification);

    }
    @Override
    public Specification getSpecification(long id) {
        Optional< Specification > optional = specificationRepository.findById(id);
        Specification specification = null;
        if (optional.isPresent()) {
            specification = optional.get();
        } else {
            throw new RuntimeException(" Specification not found for id :: " + id);
        }
        return specification;

    }

    @Override
    public Specification getLatestSpecification() {
        return specificationRepository.getLatestSpecification();
    }
}
