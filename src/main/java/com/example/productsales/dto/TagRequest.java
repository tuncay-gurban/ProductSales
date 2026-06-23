package com.example.productsales.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
public class TagRequest {
    @NotBlank(message = "Ad bos ola bilmez")
    private String name;
}
