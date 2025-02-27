package com.example.ecommerce.repositories;

import com.example.ecommerce.models.Order;
import com.example.ecommerce.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    @Query("SELECT o FROM Order o ORDER BY o.id DESC Limit 1")
    Order getLatestOrder();

}
