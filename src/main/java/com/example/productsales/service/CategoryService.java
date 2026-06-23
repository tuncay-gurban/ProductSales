package com.example.productsales.service;

import com.example.productsales.dto.CategoryRequest;
import com.example.productsales.dto.CategoryResponse;
import com.example.productsales.entity.Category;
import com.example.productsales.exception.CategoryNotFoundException;
import com.example.productsales.exception.ProductNotFoundException;
import com.example.productsales.mapper.CategoryMapper;
import com.example.productsales.repository.CategoryRepository;
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
                .orElseThrow(() -> new CategoryNotFoundException("Kateqoriya tapilmadi"));
        return mapper.toResponse(category);
    }

    public CategoryResponse getByName(String name){
        Category category = repository.findByName(name)
                .orElseThrow(()-> new CategoryNotFoundException("Kateqoriya tapilmadi"));
        return mapper.toResponse(category);
    }






    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Kateqoriya tapilmadi"));
        category.setName(request.getName());
        return mapper.toResponse(repository.save(category));
    }

    public void delete(Long id) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Kateqoriya tapilmadi"));
        repository.delete(category);
    }
}
