package com.example.productsales.repository;

import com.example.productsales.entity.Order;
import com.example.productsales.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUser(User user);
}
