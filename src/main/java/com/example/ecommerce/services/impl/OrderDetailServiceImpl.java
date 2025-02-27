package com.example.ecommerce.services.impl;

import com.example.ecommerce.models.OrderDetail;
import com.example.ecommerce.repositories.OrderDetailRepository;
import com.example.ecommerce.services.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Override
    public List<OrderDetail> getOderDetail(long orderId) {

        return orderDetailRepository.getOrderDetail(orderId);
    }
}
