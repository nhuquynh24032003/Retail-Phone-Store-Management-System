package com.example.ecommerce.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "phone")
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToMany(mappedBy = "phone", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "phone", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<PhoneRating> ratings;

    @OneToOne
    @JoinColumn(name = "spec_id")
    private Specification spec;

    // be careful cascade type all
    @OneToMany(mappedBy = "phone", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<PhoneDetail> phoneDetails;
}
