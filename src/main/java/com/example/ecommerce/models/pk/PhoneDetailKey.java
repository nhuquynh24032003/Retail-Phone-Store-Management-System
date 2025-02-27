package com.example.ecommerce.models.pk;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class PhoneDetailKey implements Serializable {
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
        PhoneDetailKey that = (PhoneDetailKey) o;
        return phoneId == that.phoneId && colorId == that.colorId && capacityId == that.capacityId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(phoneId, colorId, capacityId);
    }
}
