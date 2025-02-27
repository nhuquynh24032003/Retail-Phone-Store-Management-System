package com.example.ecommerce.models;

import com.example.ecommerce.models.pk.PhoneRatingKey;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PhoneRating {
    @EmbeddedId
    private PhoneRatingKey id;

    @JoinColumn(name = "phone_id")
    @MapsId("phone_id")
    @ManyToOne
    @JsonIgnore
    private Phone phone;

    @JoinColumn(name = "user_id")
    @MapsId("user_id")
    @ManyToOne
    private User user;

    private String img;

    private String comment;
}
