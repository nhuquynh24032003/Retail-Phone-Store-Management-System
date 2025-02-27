package com.example.ecommerce.DTO;

import com.example.ecommerce.models.Capacity;
import com.example.ecommerce.models.Color;
import lombok.*;

import java.io.Serializable;

@Data
@ToString
@RequiredArgsConstructor
public class CartItem implements Serializable {
    @NonNull
    private long phoneId;
    @NonNull
    private Capacity capacity;
    @NonNull
    private Color color;
    private String name;
    private Double price;
    private int quantity;
    private double totalPrice;
    private String img;
}


