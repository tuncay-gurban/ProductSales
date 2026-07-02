package com.example.productsales.service;

import com.example.productsales.dto.LoginRequest;
import com.example.productsales.dto.LoginResponse;
import com.example.productsales.dto.UserRequest;
import com.example.productsales.dto.UserResponse;
import com.example.productsales.entity.Cart;
import com.example.productsales.enums.Role;
import com.example.productsales.entity.User;
import com.example.productsales.exception.EmailAlreadyExistsException;
import com.example.productsales.mapper.UserMapper;
import com.example.productsales.repository.CartRepository;
import com.example.productsales.repository.UserRepository;
import com.example.productsales.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final CartRepository cartRepository;

    public UserResponse register(UserRequest request) {
        if (repository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("This email already exists");
        }
        User entity = mapper.toEntity(request);
        entity.setRoles(Set.of(Role.USER));
        entity.setPassword(encoder.encode(request.getPassword()));
        User saved = repository.save(entity);
        Cart cart = new Cart();
        cart.setUser(saved);
        cartRepository.save(cart);
        return mapper.toResponse(saved);
    }

    public LoginResponse login(LoginRequest request) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken
                (request.getEmail(), request.getPassword()));
        String token = jwtUtil.generateToken(request.getEmail());
        return LoginResponse.builder().token(token).build();
    }

}
