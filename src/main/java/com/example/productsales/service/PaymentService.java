package com.example.productsales.service;

import com.example.productsales.dto.PaymentInitResponse;
import com.example.productsales.dto.PaymentRequest;
import com.example.productsales.dto.PaymentResponse;
import com.example.productsales.entity.Order;
import com.example.productsales.entity.Payment;
import com.example.productsales.entity.User;
import com.example.productsales.enums.OrderStatus;
import com.example.productsales.enums.PaymentMethod;
import com.example.productsales.enums.PaymentStatus;
import com.example.productsales.exception.InvalidOrderStateException;
import com.example.productsales.exception.OrderNotFoundException;
import com.example.productsales.exception.UserNotFoundException;
import com.example.productsales.repository.OrderRepository;
import com.example.productsales.repository.PaymentRepository;
import com.example.productsales.repository.UserRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public User getCurrent() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Transactional
    public PaymentInitResponse pay(Long orderId, PaymentRequest request) {
        User user = getCurrent();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        if (!order.getUser().getId().equals(user.getId())) {
            throw new OrderNotFoundException("Order not found");
        }
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new InvalidOrderStateException("Order is not payable");
        }

        PaymentMethod method = request.getMethod() != null
                ? request.getMethod()
                : PaymentMethod.CARD;

        long amountInCents = order.getTotalPrice()
                .multiply(BigDecimal.valueOf(100))
                .longValue();

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amountInCents)
                .setCurrency("usd")
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .setAllowRedirects(
                                        PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER)
                                .build())
                .build();

        PaymentIntent intent;
        try {
            intent = PaymentIntent.create(params);
        } catch (StripeException e) {
            throw new RuntimeException("Stripe payment failed: " + e.getMessage());
        }

        Payment payment = Payment.builder()
                .order(order)
                .amount(order.getTotalPrice())
                .status(PaymentStatus.PENDING)
                .method(method)
                .transactionRef(intent.getId())
                .build();
        paymentRepository.save(payment);

        return PaymentInitResponse.builder()
                .paymentId(payment.getId())
                .clientSecret(intent.getClientSecret())
                .build();
    }

    public List<PaymentResponse> getPaymentsByOrder(Long orderId) {
        User user = getCurrent();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order Not Found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new OrderNotFoundException("Order not found");
        }
        return paymentRepository.findByOrder(order).stream()
                .map(this::toResponse)
                .toList();

    }

    @Transactional
    public void handlePaymentSuccess(String paymentIntendId) {
        Payment payment = paymentRepository.findByTransactionRef(paymentIntendId)
                .orElseThrow(() -> new RuntimeException("Payment not found: " + paymentIntendId));
        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            return;
        }

        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaidAt(LocalDateTime.now());
        paymentRepository.save(payment);

        Order order = payment.getOrder();
        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);
    }


    private PaymentResponse toResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .orderId(payment.getOrder().getId())
                .amount(payment.getAmount())
                .status(payment.getStatus().name())
                .method(payment.getMethod().name())
                .paidAt(payment.getPaidAt())
                .transactionRef(payment.getTransactionRef())
                .build();
    }
}
