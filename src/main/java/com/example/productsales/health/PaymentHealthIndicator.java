package com.example.productsales.health;


import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class PaymentHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        boolean paymentServiceUp = true;
        if (paymentServiceUp) {
            return Health.up()
                    .withDetail("service", "Payment Gateway")
                    .withDetail("status", "Available")
                    .build();
        } else {
            return Health.down()
                    .withDetail("service", "Payment Gateway")
                    .withDetail("error", "Cannot connect to payment provider")
                    .build();
        }
    }
}
