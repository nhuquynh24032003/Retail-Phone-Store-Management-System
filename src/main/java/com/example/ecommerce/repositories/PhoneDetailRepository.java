package com.example.ecommerce.repositories;

import com.example.ecommerce.models.Color;
import com.example.ecommerce.models.Phone;
import com.example.ecommerce.models.PhoneDetail;
import com.example.ecommerce.models.PhoneRating;
import com.example.ecommerce.models.User;
import com.example.ecommerce.models.pk.PhoneDetailKey;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface PhoneDetailRepository extends JpaRepository<PhoneDetail, PhoneDetailKey> {
    List<PhoneDetail> findFirst5ByOrderByCreatedAtDesc();

    List<PhoneDetail> findAllByPhone(Phone phone);

    List<PhoneDetail> findByColorAndPriceLessThan(Color color, int price);

    List<PhoneDetail> findByColorAndPriceBetween(Color color, int lowest, int highest);

    List<PhoneDetail> findByColorAndPriceGreaterThan(Color color, int price);

    List<PhoneDetail> findByPriceLessThan(int price);

    List<PhoneDetail> findByPriceBetween(int lowest, int highest);

    List<PhoneDetail> findByPriceGreaterThan(int price);

    List<PhoneDetail> findByColor(Color color);

    @Modifying
    @Transactional
    @Query("DELETE FROM PhoneDetail pd WHERE pd.phone.id = :phoneId AND pd.color.id = :colorId AND pd.capacity.id = :capacityId")
    void deleteByPhoneDetail(@Param("phoneId") Long phoneId, @Param("colorId") Long colorId, @Param("capacityId") Long capacityId);

    @Query("SELECT pd FROM PhoneDetail pd where pd.phone.id = :phoneId AND pd.color.id = :colorId AND pd.capacity.id = :capacityId")
    PhoneDetail getPhoneDetail(@Param("phoneId") Long phoneId, @Param("colorId") Long colorId, @Param("capacityId") Long capacityId);

    @Query("SELECT pd FROM PhoneDetail pd WHERE CONCAT(pd.price, pd.capacity, pd.color, pd.quantity, pd.phone.spec.batteryLife, pd.phone.brand.name, pd.phone.spec.camera, pd.phone.spec.connection, pd.phone.spec.cpu, pd.phone.spec.display, pd.phone.spec.os, pd.phone.spec.ram, pd.phone.description, pd.phone.name) LIKE %?1%")
    List<PhoneDetail> search(String keyword);

}
