package com.example.productsales.repository;

import com.example.productsales.entity.Order;
import com.example.productsales.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByOrder(Order order);

    Optional<Payment> findByTransactionRef(String transactionRef);
}
