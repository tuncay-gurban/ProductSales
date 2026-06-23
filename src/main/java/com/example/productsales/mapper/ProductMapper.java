package com.example.test2.mapper;

import com.example.test2.dto.ProductRequest;
import com.example.test2.dto.ProductResponse;
import com.example.test2.entity.Product;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toEntity(ProductRequest request);

    ProductResponse toResponse(Product product);

    List<ProductResponse> toResponseList(List<Product> products);
}
