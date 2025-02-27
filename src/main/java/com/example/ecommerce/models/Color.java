package com.example.ecommerce.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="color")
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    @OneToMany(mappedBy = "color", fetch= FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PhoneDetail> phoneDetails;

    @OneToMany(mappedBy = "color", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OrderDetail> orderDetails;

    public Color(String name) {
        this.name = name;
    }
}
