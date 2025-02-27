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
@Table(name="capacity")
public class Capacity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="size_ingb")
    private int sizeInGB;

    @OneToMany(mappedBy = "capacity", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<PhoneDetail> phoneDetails;

    @OneToMany(mappedBy = "capacity", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OrderDetail> orderDetails;

    public Capacity(int sizeInGB) {
        this.sizeInGB = sizeInGB;
    }
}
