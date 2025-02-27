package com.example.ecommerce.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vouchers")
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private double percentDiscount;

    private Date expireTime;

    private boolean isUse;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    @JoinColumn(referencedColumnName = "id")
    @OneToOne
    private Order order;

    public Voucher(double percentDiscount, Date expireTime, boolean isUse) {
        this.percentDiscount = percentDiscount;
        this.expireTime = expireTime;
        this.isUse = isUse;
    }
}
