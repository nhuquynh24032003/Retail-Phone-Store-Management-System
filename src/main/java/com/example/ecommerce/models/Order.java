package com.example.ecommerce.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Date createAt;
    private double totalSellingPrice;
    private byte status;
    private double amountPaid;
    private byte paymentMethod;
    private String note;
    private byte deliveryStatus;


    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    @OneToOne(mappedBy = "order")
    private Voucher voucher;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;
}
