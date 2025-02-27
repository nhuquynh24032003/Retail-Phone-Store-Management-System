package com.example.ecommerce.services.impl;

import com.example.ecommerce.models.Color;
import com.example.ecommerce.models.Phone;
import com.example.ecommerce.models.PhoneDetail;
import com.example.ecommerce.models.pk.PhoneDetailKey;
import com.example.ecommerce.repositories.PhoneDetailRepository;
import com.example.ecommerce.services.PhoneDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.Optional;

@Service
public class PhoneDetailServiceImpl implements PhoneDetailService {
    private final PhoneDetailRepository phoneDetailRepository;

    @Autowired
    public PhoneDetailServiceImpl(PhoneDetailRepository phoneDetailRepository) {
        this.phoneDetailRepository = phoneDetailRepository;
    }

    @Override
    public void addPhoneDetail(PhoneDetail phoneDetail) {
        phoneDetailRepository.save(phoneDetail);
    }

    @Override
    public List<PhoneDetail> getAll() {

        return phoneDetailRepository.findAll();
    }

    @Override
    public List<PhoneDetail> getAllPhoneDetail(String keyword) {
        if (keyword != null) {
            return phoneDetailRepository.search(keyword);
        }
        return phoneDetailRepository.findAll();
    }

    @Override
    public PhoneDetail getPhoneDetailByID(PhoneDetailKey id) {
        Optional<PhoneDetail> optional = phoneDetailRepository.findById(id);
        PhoneDetail phoneDetail = null;
        if (optional.isPresent()) {
            phoneDetail = optional.get();
        } else {
            throw new RuntimeException(" Phone detail not found for id :: " + id);
        }
        return phoneDetail;
    }
    @Override
    public PhoneDetail getPhoneDetail(long phoneId, long colorId, long capacityId){
       return phoneDetailRepository.getPhoneDetail(phoneId, colorId, capacityId);
    }
    @Override
    public void deletePhoneDetail(long phoneId, long colorId, long capacityId) {
        phoneDetailRepository.deleteByPhoneDetail(phoneId, colorId, capacityId);
    }


}

