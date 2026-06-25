package com.example.productsales.controller;

import com.example.productsales.dto.CartItemrequest;
import com.example.productsales.dto.CartResponse;
import com.example.productsales.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService service;

    @GetMapping
    public ResponseEntity<CartResponse> getCart() {
        return ResponseEntity.ok(service.getCart());
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItem(@RequestBody CartItemrequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addItem(request));
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<CartResponse> updateItem(@RequestBody CartItemrequest request, @PathVariable Long id) {
        return ResponseEntity.ok(service.updateItem(id, request));
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<CartResponse> removeItem(@PathVariable Long id) {
        return ResponseEntity.ok(service.removeItem(id));
    }

    @DeleteMapping
    public ResponseEntity<CartResponse> clearCart() {
        return ResponseEntity.ok(service.clearCart());
    }
}
