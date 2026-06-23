package com.example.productsales.repository;

import com.example.productsales.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagReppository extends JpaRepository<Tag, Long> {
}
