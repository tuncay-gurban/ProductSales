package com.example.productsales.repository;

import com.example.productsales.entity.Cart;
import com.example.productsales.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {
    Optional<Cart> findByUser(User user);

}
