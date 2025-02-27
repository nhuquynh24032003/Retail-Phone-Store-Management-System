package com.example.ecommerce.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="specification")
public class Specification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String ram;
    private String os;
    private String cpu;
    private String display;
    private String camera;
    private String batteryLife;
    private String connection;

    @OneToOne(mappedBy = "spec")
    private Phone phone;
}
