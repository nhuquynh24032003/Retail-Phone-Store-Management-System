package com.example.ecommerce.services;

import com.example.ecommerce.models.Order;
import com.example.ecommerce.models.User;

import java.util.List;

public interface OrderService {
    void createOrder();

    List<Order> getAllOrders();

    List<Order> getOrderByUser(User user);

    Order getById(Long id);

    void update(Long id, Order newOrder);

    void delete(Long id);
    void saveOrder(Order order);
}
