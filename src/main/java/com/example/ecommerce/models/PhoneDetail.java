package com.example.ecommerce.models;

import com.example.ecommerce.models.pk.PhoneDetailKey;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "phone_detail")
public class PhoneDetail {
    @EmbeddedId
    private PhoneDetailKey id;

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

    private String img;

    private double price;

    private int quantity;

    private Date createdAt;

    private Date updatedAt;

    public PhoneDetail(PhoneDetailKey id, String img, double price, int quantity) {
        this.id = id;
        this.img = img;
        this.price = price;
        this.quantity = quantity;
    }
}
