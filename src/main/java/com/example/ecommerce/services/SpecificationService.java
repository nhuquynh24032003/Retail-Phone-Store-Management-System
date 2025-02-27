package com.example.ecommerce.services;

import com.example.ecommerce.models.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SpecificationService {
    List< Specification > getAllSpecification();
    void deleteSpecification(long id);
    void saveSpecification(Specification specification);
    Specification getSpecification(long id);

    Specification getLatestSpecification();
}
