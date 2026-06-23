package com.example.productsales.mapper;

import com.example.productsales.dto.ProductRequest;
import com.example.productsales.dto.ProductResponse;
import com.example.productsales.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "tags",ignore = true)
    Product toEntity(ProductRequest request);

    ProductResponse toResponse(Product product);

    List<ProductResponse> toResponseList(List<Product> products);
}
