package com.example.test2.service;

import com.example.test2.dto.ProductRequest;
import com.example.test2.dto.ProductResponse;
import com.example.test2.entity.Product;
import com.example.test2.entity.ProductDetail;
import com.example.test2.exception.ProductNotFoundException;
import com.example.test2.mapper.ProductMapper;
import com.example.test2.repository.ProductDetailRepository;
import com.example.test2.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;
    private final ProductMapper mapper;
    private final ProductDetailRepository productDetailRepository;

    public ProductResponse create(ProductRequest request) {
        Product entity = mapper.toEntity(request);
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
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        return mapper.toResponse(repository.save(product));
    }


    public void delete(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Mehsul tapilmadi"));
        repository.delete(product);
    }

//    @Transactional
//    public Product createProductWithDetail(String description,Double weight){
//        ProductDetail detail = new ProductDetail();
//        detail.setDescription(description);
//        detail.setWeight(weight);
//        productDetailRepository.save(detail);
//
//        Product product = new Product();
//        product.setName("Samsung");
//        product.setPrice(2500.0);
//        product.setProductDetail(detail);
//        return repository.save(product);
//    }
//
//    @Transactional(readOnly = true)
//    public List<Product> getAllProducts(){
//         return repository.findAll();
//    }

}
