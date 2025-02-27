package com.example.ecommerce.services.impl;

import com.example.ecommerce.DTO.CartItem;
import com.example.ecommerce.models.Order;
import com.example.ecommerce.models.OrderDetail;
import com.example.ecommerce.models.User;
import com.example.ecommerce.models.pk.OrderDetailKey;
import com.example.ecommerce.repositories.*;
import com.example.ecommerce.services.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    HttpSession session;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    PhoneDetailRepository phoneDetailRepository;
    @Autowired
    private PhoneRepository phoneRepository;
    @Autowired
    private ColorRepository colorRepository;
    @Autowired
    private CapacityRepository capacityRepository;

    @Override
    public void createOrder() {
        // create order
        Order order = new Order();
        User user = (User) session.getAttribute("user");
        Map<Integer, CartItem> cart = (HashMap<Integer, CartItem>) session.getAttribute("cart");
        Double totalSellingPrice = cart.values().stream().mapToDouble(CartItem::getTotalPrice).sum();

        order.setUser(user);
        order.setCreateAt(new Date(System.currentTimeMillis()));
        order.setTotalSellingPrice(totalSellingPrice);
        order.setStatus((byte)0); // CREATED

        orderRepository.save(order);

        order = orderRepository.getLatestOrder();

        // add order detail
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (CartItem cartItem : cart.values()) {

            OrderDetailKey orderDetailKey = new OrderDetailKey(order.getId(), cartItem.getPhoneId(), cartItem.getColor().getId(), cartItem.getCapacity().getId());
            orderDetails.add(new OrderDetail(orderDetailKey, order, phoneRepository.findById(cartItem.getPhoneId()).get(), colorRepository.findById(cartItem.getColor().getId()).get(), capacityRepository.findById(cartItem.getCapacity().getId()).get(), cartItem.getQuantity()));
        }
        order.setOrderDetails(orderDetails);
        orderRepository.save(order);

        // reset cart
        session.removeAttribute("cart");
        session.removeAttribute("finalTotalPrice");
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrderByUser(User user) {
        return orderRepository.findByUser(user);
    }

    @Override
    public Order getById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public void update(Long id, Order newOrder) {
        Order orderFound = orderRepository.findById(id).orElse(null);
        if (orderFound != null) {
            BeanUtils.copyProperties(newOrder, orderFound, "id");
            orderRepository.save(orderFound);
        }
    }

    @Override
    public void delete(Long id) {
        orderRepository.deleteById(id);
    }
    public void saveOrder(Order oder){
        orderRepository.save(oder);
    }



}
