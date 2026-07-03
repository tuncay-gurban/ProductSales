package com.example.productsales.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentInitResponse {
    private Long paymentId;
    private String clientSecret;
}
