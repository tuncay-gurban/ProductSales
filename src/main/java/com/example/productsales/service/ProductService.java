package com.example.productsales.service;

import com.example.productsales.dto.ProductRequest;
import com.example.productsales.dto.ProductResponse;
import com.example.productsales.entity.Category;
import com.example.productsales.entity.Product;
import com.example.productsales.entity.Tag;
import com.example.productsales.exception.CategoryNotFoundException;
import com.example.productsales.exception.ProductNotFoundException;
import com.example.productsales.mapper.ProductMapper;
import com.example.productsales.repository.CategoryRepository;
import com.example.productsales.repository.ProductRepository;
import com.example.productsales.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;
    private final ProductMapper mapper;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    public ProductResponse create(ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Bu id ile kateqoriya tapilmadi" + request.getCategoryId()));
        List<Tag> tags = tagRepository.findAllById(request.getTagIds());
        Product entity = mapper.toEntity(request);
        entity.setCategory(category);
        entity.setTags(new HashSet<>(tags));
        Product saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public List<ProductResponse> getAll() {
        List<Product> products = repository.findAll();
        return mapper.toResponseList(products);
    }

    public ProductResponse getId(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Mehsul tapilmadi"));
        return mapper.toResponse(product);
    }

    public ProductResponse getByName(String name) {
        Product product = repository.findByName(name)
                .orElseThrow(() -> new ProductNotFoundException("Mehsul tapilmadi"));
        return mapper.toResponse(product);
    }


    public ProductResponse update(ProductRequest request, Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Mehsul tapilmadi"));
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Kateqoriya tapilmadi"));
        List<Tag> tags = tagRepository.findAllById(request.getTagIds());
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setWeight(request.getWeight());
        product.setCategory(category);
        product.setTags(new HashSet<>(tags));

        return mapper.toResponse(repository.save(product));
    }


    public void delete(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Mehsul tapilmadi"));
        repository.delete(product);
    }


}
