package com.example.productsales.mapper;


import com.example.productsales.dto.CategoryRequest;
import com.example.productsales.dto.CategoryResponse;
import com.example.productsales.entity.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toEntity(CategoryRequest request);

    CategoryResponse toResponse(Category category);

    List<CategoryResponse> toResponseList(List<Category> categories);
}
