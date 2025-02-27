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
public class PhoneRatingKey implements Serializable {
    @Column(name = "user_id")
    private long userId;

    @Column(name = "phone_id")
    private long phoneId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneRatingKey that = (PhoneRatingKey) o;
        return userId == that.userId && phoneId == that.phoneId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, phoneId);
    }
}
