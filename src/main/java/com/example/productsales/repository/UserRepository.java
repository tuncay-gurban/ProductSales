package com.example.productsales.repository;

import com.example.productsales.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);

    boolean existsByUserName(String userName);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
