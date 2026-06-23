package com.example.test2.service;

import com.example.test2.dto.CategoryRequest;
import com.example.test2.dto.ProductDetailRequest;
import com.example.test2.dto.CategoryResponse;
import com.example.test2.entity.Category;
import com.example.test2.exception.ProductNotFoundException;
import com.example.test2.mapper.CategoryMapper;
import com.example.test2.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    public CategoryResponse create(CategoryRequest request) {
        Category entity = mapper.toEntity(request);
        Category saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public List<CategoryResponse> getAll() {

        return mapper.toResponseList(repository.findAll());
    }

    public CategoryResponse getId(Long id) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Category tapilmadi"));
        return mapper.toResponse(category);
    }

    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Category tapilmadi"));
        category.setName(request.getName());
        return mapper.toResponse(repository.save(category));
    }

    public void delete(Long id) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Category tapilmadi"));
        repository.delete(category);
    }
}
