package com.example.productsales.mapper;

import com.example.productsales.dto.UserRequest;
import com.example.productsales.dto.UserResponse;
import com.example.productsales.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRequest request);

    UserResponse toResponse(User user);
}
