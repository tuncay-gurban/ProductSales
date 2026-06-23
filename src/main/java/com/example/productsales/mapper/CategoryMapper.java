package com.example.test2.mapper;


import com.example.test2.dto.CategoryRequest;
import com.example.test2.dto.CategoryResponse;
import com.example.test2.entity.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toEntity(CategoryRequest request);

    CategoryResponse toResponse(Category category);

    List<CategoryResponse> toResponseList(List<Category> categories);
}
