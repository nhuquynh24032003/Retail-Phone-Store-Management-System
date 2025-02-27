package com.example.ecommerce.services;

import com.example.ecommerce.models.Phone;
import com.example.ecommerce.models.PhoneDetail;
import com.example.ecommerce.models.pk.PhoneDetailKey;

import java.util.List;

import java.util.List;

public interface PhoneDetailService {
    void addPhoneDetail(PhoneDetail phoneDetail);

    List <PhoneDetail> getAll();
    List <PhoneDetail> getAllPhoneDetail(String keyword);
    PhoneDetail getPhoneDetailByID(PhoneDetailKey phoneDetailKey);
    void deletePhoneDetail(long phoneId, long colorId, long capacityId);
    PhoneDetail getPhoneDetail(long phoneId, long colorId, long capacityId);
}
