package com.example.productsales.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
public class LoginRequest {
    @NotBlank(message = "Email bos ola bilmez")
    @Email
    private String email;

    @NotBlank(message = "Password bos ola bilmez")
    private String password;
}
