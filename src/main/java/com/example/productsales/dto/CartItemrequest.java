package com.example.productsales.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemrequest {
    private Long productId;
    private int quantity;
}
