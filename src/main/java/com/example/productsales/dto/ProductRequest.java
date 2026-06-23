package com.example.productsales.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
public class ProductRequest {
    @NotBlank(message = "ad bos ola bilmez")
    private String name;

    @Positive(message = "qiymet musbet olmalidir")
    private BigDecimal price;

    private String description;
    private Double weight;

    @NotNull(message = "Kateqoriya secilmelidir")
    private Long categoryId;

    private Set<Long> tagIds;
}
