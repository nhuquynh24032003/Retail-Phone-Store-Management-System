package com.example.ecommerce.services;

import com.example.ecommerce.models.EmailDetail;
import org.springframework.stereotype.Service;

@Service
public interface EmailDetailService {

    String sendSimpleMail(EmailDetail emailDetail);
}
