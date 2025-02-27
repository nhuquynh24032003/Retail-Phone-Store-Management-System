package com.example.ecommerce.services;

import com.example.ecommerce.models.OrderDetail;

public interface OrderDetailService {
    <List> java.util.List<OrderDetail> getOderDetail(long orderId);
}
