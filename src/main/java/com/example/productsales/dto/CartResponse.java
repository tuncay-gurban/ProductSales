package com.example.productsales.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class CartResponse {
    private Long id;
    private List<CartItemResponse> items;
}
