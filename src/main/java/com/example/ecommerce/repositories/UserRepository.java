package com.example.ecommerce.repositories;

import com.example.ecommerce.models.Order;
import com.example.ecommerce.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);

    List<User> findByEmail(String email);

    @Query("SELECT u FROM User u where u.status = false")
    List < User > getUsersBlock();

    @Query("SELECT u FROM User u where u.status = true and u.role = 'ROLE_USER'")
    List < User > getUsers();
    @Query("SELECT u FROM User u WHERE CONCAT(u.name, u.address, u.email, u.phone) LIKE %?1% AND u.status = true")
    List< User > search(String keyword);

    @Query("SELECT u FROM User u WHERE CONCAT(u.name, u.address, u.email, u.phone) LIKE %?1% AND u.status = false")
    List< User > searchBlock(String keyword);
}