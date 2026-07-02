package com.example.productsales.controller;

import com.example.productsales.dto.OrderResponse;
import com.example.productsales.dto.PaymentRequest;
import com.example.productsales.dto.PaymentResponse;
import com.example.productsales.service.OrderService;
import com.example.productsales.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService service;
    private final PaymentService paymentService;

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout() {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.checkout());
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getMyOrders() {
        return ResponseEntity.ok(service.getMyOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getOrderById(id));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long id) {
        return ResponseEntity.ok(service.cancelOrder(id));
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<PaymentResponse> pay(@PathVariable Long id, @RequestBody PaymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.pay(id, request));
    }

    @GetMapping("/{id}/payment")
    public ResponseEntity<List<PaymentResponse>> getPayments(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getPaymentsByOrder(id));
    }


}
