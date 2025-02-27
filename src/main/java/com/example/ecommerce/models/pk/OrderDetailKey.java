package com.example.ecommerce.models.pk;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class OrderDetailKey implements Serializable {
    @Column(name = "order_id")
    private long orderId;

    @Column(name = "phone_id")
    private long phoneId;

    @Column(name = "color_id")
    private long colorId;

    @Column(name = "capacity_id")
    private long capacityId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDetailKey that = (OrderDetailKey) o;
        return orderId == that.orderId && phoneId == that.phoneId && colorId == that.colorId && capacityId == that.capacityId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, phoneId, colorId, capacityId);
    }
}
