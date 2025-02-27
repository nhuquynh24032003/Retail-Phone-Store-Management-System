package com.example.ecommerce.repositories;

import com.example.ecommerce.models.Capacity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CapacityRepository extends JpaRepository<Capacity, Long> {
    Capacity findCapacityBySizeInGB(int sizeInGB);
}
