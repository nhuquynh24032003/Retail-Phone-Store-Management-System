package com.example.ecommerce.models;

import com.example.ecommerce.models.pk.OrderDetailKey;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_detail")
public class    OrderDetail {
    @EmbeddedId
    private OrderDetailKey id;

    @JoinColumn(name = "order_id")
    @MapsId("order_id")
    @ManyToOne
    private Order order;

    @JoinColumn(name = "phone_id")
    @MapsId("phone_id")
    @ManyToOne
    @JsonIgnore
    private Phone phone;

    @JoinColumn(name = "color_id")
    @MapsId("color_id")
    @ManyToOne
    private Color color;

    @JoinColumn(name = "capacity_id")
    @MapsId("capacity_id")
    @ManyToOne
    private Capacity capacity;

    private int quantity;
}
