package com.example.productsales.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CartItemResponse {
    private Long id;
    private Long productId;
    private int quantity;
}
