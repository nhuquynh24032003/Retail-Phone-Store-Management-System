package com.example.ecommerce.services.impl;

import com.example.ecommerce.DTO.CartItem;
import com.example.ecommerce.models.Capacity;
import com.example.ecommerce.models.Color;
import com.example.ecommerce.models.PhoneDetail;
import com.example.ecommerce.models.pk.PhoneDetailKey;
import com.example.ecommerce.repositories.CapacityRepository;
import com.example.ecommerce.repositories.ColorRepository;
import com.example.ecommerce.repositories.PhoneDetailRepository;
import com.example.ecommerce.services.CartService;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@SessionScope
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    HttpSession session;

    @Autowired
    PhoneDetailRepository phoneDetailRepository;

    @Autowired
    private ColorRepository colorRepository;

    @Autowired
    private CapacityRepository capacityRepository;

    @Override
    public void addToCart(long phoneId, long colorId, long capacityId) {
        Color colorFound = colorRepository.findById(colorId).get();
        Capacity capacityFound = capacityRepository.findById(capacityId).get();

        PhoneDetailKey phoneDetailKey = new PhoneDetailKey(phoneId, colorId, capacityId);
        PhoneDetail phoneDetailFound = phoneDetailRepository.findById(phoneDetailKey).get();

        Map<PhoneDetailKey, CartItem> cart = (HashMap<PhoneDetailKey, CartItem>) session.getAttribute("cart");

        if (cart == null) {
            cart = new HashMap<>();
        }

        if (cart.get(phoneDetailKey) == null) {
            CartItem cartItem = new CartItem(phoneId, capacityFound, colorFound);
            cartItem.setCapacity(capacityFound);
            cartItem.setColor(colorFound);
            cartItem.setQuantity(1);
            cartItem.setTotalPrice(phoneDetailFound.getPrice());
            cartItem.setName(phoneDetailFound.getPhone().getName());
            cartItem.setPrice(phoneDetailFound.getPrice());
            cartItem.setImg(phoneDetailFound.getImg());
            cart.put(phoneDetailKey, cartItem);
        } else {
            CartItem cartItem = cart.get(phoneDetailKey);
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            cartItem.setTotalPrice(phoneDetailFound.getPrice() * cartItem.getQuantity());
        }

        System.out.println(cart);
        session.setAttribute("cart", cart);
        Double totalSellingPrice = cart.values().stream().mapToDouble(CartItem::getTotalPrice).sum();
        session.setAttribute("finalTotalPrice", totalSellingPrice);
    }

    @Override
    public void clear() {
        session.removeAttribute("cart");
        session.removeAttribute("finalTotalPrice");
        System.out.println(session.getAttribute("cart"));
    }

    @Override
    public void update(long phoneId, long colorId, long capacityId,  int quantity) {
        PhoneDetailKey phoneDetailKey = new PhoneDetailKey(phoneId, colorId, capacityId);
        PhoneDetail phoneDetailFound = phoneDetailRepository.findById(phoneDetailKey).get();
        Map<PhoneDetailKey, CartItem> cart = (HashMap<PhoneDetailKey, CartItem>) session.getAttribute("cart");
        CartItem cartItem = cart.get(phoneDetailKey);
        cartItem.setQuantity(quantity);
        cartItem.setTotalPrice(phoneDetailFound.getPrice() * cartItem.getQuantity());
        System.out.println(cart);
        Double totalSellingPrice = cart.values().stream().mapToDouble(CartItem::getTotalPrice).sum();
        session.setAttribute("finalTotalPrice", totalSellingPrice);
    }

    @Override
    public void remove(long phoneId, long colorId, long capacityId) {
        PhoneDetailKey phoneDetailKey = new PhoneDetailKey(phoneId, colorId, capacityId);
        Map<Integer, CartItem> cart = (HashMap<Integer, CartItem>) session.getAttribute("cart");
        cart.remove(phoneDetailKey);
        session.setAttribute("cart", cart);
        System.out.println(cart);
        Double totalSellingPrice = cart.values().stream().mapToDouble(CartItem::getTotalPrice).sum();
        session.setAttribute("finalTotalPrice", totalSellingPrice);
    }
}

