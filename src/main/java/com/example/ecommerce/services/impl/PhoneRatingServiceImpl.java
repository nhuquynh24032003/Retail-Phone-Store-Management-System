package com.example.ecommerce.services.impl;

import com.example.ecommerce.models.Phone;
import com.example.ecommerce.models.PhoneRating;
import com.example.ecommerce.models.User;
import com.example.ecommerce.models.pk.PhoneRatingKey;
import com.example.ecommerce.repositories.PhoneRatingRepository;
import com.example.ecommerce.services.PhoneRatingService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class PhoneRatingServiceImpl implements PhoneRatingService {
    private final PhoneRatingRepository phoneRatingRepository;

    public PhoneRatingServiceImpl(PhoneRatingRepository phoneRatingRepository) {
        this.phoneRatingRepository = phoneRatingRepository;
    }

    @Override
    public void saveRating(Phone phone, User user, String img, String comment) {
        PhoneRating phoneRating = new PhoneRating();
        PhoneRatingKey phoneRatingKey = new PhoneRatingKey(user.getId(), phone.getId());
        phoneRating.setId(phoneRatingKey);
        phoneRating.setPhone(phone);
        phoneRating.setUser(user);
        phoneRating.setImg(img);
        phoneRating.setComment(comment);
        phoneRatingRepository.save(phoneRating);
    }

    @Override
    public List<PhoneRating> getPhoneRatingByUserAndPhone(User user, Phone phone) {
        return phoneRatingRepository.findByUserAndPhone(user, phone);
    }

    @Override
    public List<PhoneRating> getAllPhoneRating() {
        return phoneRatingRepository.findAll();
    }

    @Override
    public List<PhoneRating> getRatingByPhone(Phone phone) {
        return phoneRatingRepository.findByPhone(phone);
    }
}
