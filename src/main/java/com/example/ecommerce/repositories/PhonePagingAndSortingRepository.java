package com.example.ecommerce.repositories;

import com.example.ecommerce.models.Brand;
import com.example.ecommerce.models.Phone;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PhonePagingAndSortingRepository extends PagingAndSortingRepository<Phone, Integer> {
    List<Phone> findAllByBrand(Brand brand, Pageable pageable);
}
