package com.example.ecommerce.repositories;

import com.example.ecommerce.models.Brand;
import com.example.ecommerce.models.Color;
import com.example.ecommerce.models.Phone;
import com.example.ecommerce.models.PhoneDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long> {
    List<Phone> findByBrand(Brand brand);

    List<Phone> findByBrandAndIdNot(Brand brand, long id);

    List<Phone> findPhoneByNameContainsIgnoreCase(String name);

    List<Phone> findPhoneByPhoneDetails(PhoneDetail phoneDetail);


    @Query("SELECT p FROM Phone p ORDER BY p.id DESC Limit 1")
    Phone getLatestPhone();
}
