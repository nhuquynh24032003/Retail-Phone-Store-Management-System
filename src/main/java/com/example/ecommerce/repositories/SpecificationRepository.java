package com.example.ecommerce.repositories;

import com.example.ecommerce.models.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecificationRepository extends JpaRepository<Specification, Long> {
    @Query("SELECT s FROM Specification s ORDER BY s.id DESC Limit 1")
    Specification getLatestSpecification();
}
