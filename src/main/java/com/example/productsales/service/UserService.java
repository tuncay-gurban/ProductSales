package com.example.productsales.service;

import com.example.productsales.dto.LoginRequest;
import com.example.productsales.dto.LoginResponse;
import com.example.productsales.dto.UserRequest;
import com.example.productsales.dto.UserResponse;
import com.example.productsales.entity.Role;
import com.example.productsales.entity.User;
import com.example.productsales.exception.EmailAlreadyExistsException;
import com.example.productsales.mapper.UserMapper;
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

    public UserResponse register(UserRequest request) {
        if (repository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Bu email ile artiq register olunub");
        }
        User entity = mapper.toEntity(request);
        entity.setRoles(Set.of(Role.USER));
        entity.setPassword(encoder.encode(request.getPassword()));
        return mapper.toResponse(repository.save(entity));
    }

    public LoginResponse login(LoginRequest request) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken
                (request.getEmail(), request.getPassword())
        );
        String token = jwtUtil.generateToken(request.getEmail());
        return LoginResponse.builder().token(token).build();
    }

}
