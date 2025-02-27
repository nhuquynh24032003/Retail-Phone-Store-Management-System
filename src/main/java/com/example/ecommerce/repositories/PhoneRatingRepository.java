package com.example.ecommerce.repositories;

import com.example.ecommerce.models.Phone;
import com.example.ecommerce.models.PhoneRating;
import com.example.ecommerce.models.User;
import com.example.ecommerce.models.pk.PhoneRatingKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhoneRatingRepository extends JpaRepository<PhoneRating, PhoneRatingKey> {
    List<PhoneRating> findByUserAndPhone(User user, Phone phone);

    List<PhoneRating> findByPhone(Phone phone);
}
