package com.example.productsales.service;

import com.example.productsales.dto.OrderItemResponse;
import com.example.productsales.dto.OrderResponse;
import com.example.productsales.entity.*;
import com.example.productsales.enums.OrderStatus;
import com.example.productsales.exception.CartNotFoundException;
import com.example.productsales.exception.InvalidOrderStateException;
import com.example.productsales.exception.OrderNotFoundException;
import com.example.productsales.exception.UserNotFoundException;
import com.example.productsales.repository.CartRepository;
import com.example.productsales.repository.OrderRepository;
import com.example.productsales.repository.UserRepository;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final MeterRegistry meterRegistry;

    private User getCurrent() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Transactional
    public OrderResponse checkout() {
        User user = getCurrent();

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is Empty");
        }

        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .items(new ArrayList<>())
                .totalPrice(BigDecimal.ZERO)
                .build();


        for (CartItem cartItem : cart.getItems()) {
            BigDecimal itemPrice = cartItem.getProduct().getPrice();

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(cartItem.getProduct())
                    .quantity(cartItem.getQuantity())
                    .price(itemPrice)
                    .build();
            order.getItems().add(orderItem);

            BigDecimal lineTotal = itemPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            order.setTotalPrice(order.getTotalPrice().add(lineTotal));
        }
        Order saved = orderRepository.save(order);
        cart.getItems().clear();
        cartRepository.save(cart);
        meterRegistry.counter("orders.created").increment();
        return toResponse(saved);
    }

    public List<OrderResponse> getMyOrders() {
        User user = getCurrent();
        return orderRepository.findByUser(user).stream()
                .map(this::toResponse)
                .toList();
    }

    public OrderResponse getOrderById(Long id) {
        User user = getCurrent();
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        if (!order.getUser().getId().equals(user.getId())) {
            throw new OrderNotFoundException("Order not found");
        }
        return toResponse(order);
    }

    @Transactional
    public OrderResponse cancelOrder(Long id) {
        User user = getCurrent();
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        if (!order.getUser().getId().equals(user.getId())) {
            throw new OrderNotFoundException("Order not found");
        }
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new InvalidOrderStateException("Order cannot be cancelled");
        }
        order.setStatus(OrderStatus.CANCELLED);
        Order saved = orderRepository.save(order);
        return toResponse(saved);
    }

    private OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(item -> OrderItemResponse.builder()
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .build())
                .toList();
        return OrderResponse.builder()
                .id(order.getId())
                .status(order.getStatus().name())
                .totalPrice(order.getTotalPrice())
                .createdAt(order.getCreatedAt())
                .items(items)
                .build();
    }
}
