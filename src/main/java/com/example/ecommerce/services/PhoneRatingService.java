package com.example.ecommerce.services;

import com.example.ecommerce.models.Phone;
import com.example.ecommerce.models.PhoneRating;
import com.example.ecommerce.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PhoneRatingService {
    void saveRating(Phone phone, User user, String imgFile, String comment);

    List<PhoneRating> getPhoneRatingByUserAndPhone(User user, Phone phone);

    List<PhoneRating> getAllPhoneRating();

    List<PhoneRating> getRatingByPhone(Phone phone);
}
