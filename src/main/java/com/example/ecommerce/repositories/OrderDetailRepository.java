package com.example.ecommerce.repositories;

import com.example.ecommerce.models.OrderDetail;
import com.example.ecommerce.models.PhoneDetail;
import com.example.ecommerce.models.pk.OrderDetailKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailKey> {
    @Query("SELECT od FROM OrderDetail od where od.id.orderId = :orderId")
    List<OrderDetail> getOrderDetail(@Param("orderId") Long orderId);
}
