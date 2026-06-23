package com.example.productsales.repository;

import com.example.productsales.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findById(Long id);

    Optional<Product> findByName(String name);// bunu duzelt optionaldan cixart List<> ele

}
