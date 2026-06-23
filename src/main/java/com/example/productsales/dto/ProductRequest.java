package com.example.test2.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {
    @NotBlank(message = "ad bos ola bilmez")
    private String name;

    @Positive(message = "qiymet musbet olmalidir")
    private Double price;
}
