package com.example.productsales.dto;

import com.example.productsales.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;


@Getter
@Setter
public class UserRequest {
    @NotBlank(message = "Username bos ola bilmez")
    private String userName;

    @NotBlank(message = "Email bos ola bilmez")
    @Email
    private String email;

    @NotBlank(message = "Password bos ola bilmez")
    private String password;

    private Set<Role> roles;
}
