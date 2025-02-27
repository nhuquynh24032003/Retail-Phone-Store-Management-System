package com.example.ecommerce.services;

public interface CartService {
    void addToCart(long phoneId, long colorId, long capacityId);

    void clear();

    void update(long phoneId, long colorId, long capacityId, int quantity);

    void remove(long phoneId, long colorId, long capacityId);
}
