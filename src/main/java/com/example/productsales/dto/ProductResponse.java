package com.example.productsales.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private String description;
    private Double weight;
}
