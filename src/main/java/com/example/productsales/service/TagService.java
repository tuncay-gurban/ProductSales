package com.example.productsales.service;

import com.example.productsales.dto.TagRequest;
import com.example.productsales.dto.TagResponse;
import com.example.productsales.entity.Tag;
import com.example.productsales.exception.TagNotFoundException;
import com.example.productsales.mapper.TagMapper;
import com.example.productsales.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository repository;
    private final TagMapper mapper;

    public TagResponse create(TagRequest request) {
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    public List<TagResponse> getAll() {
        return mapper.toResponseList(repository.findAll());
    }

    public TagResponse getId(Long id) {
        Tag tag = repository.findById(id)
                .orElseThrow(() -> new TagNotFoundException("Tag not found"));
        return mapper.toResponse(tag);
    }

    public TagResponse getByName(String name) {
        Tag tag = repository.findByName(name)
                .orElseThrow(() -> new TagNotFoundException("Tag not found"));
        return mapper.toResponse(tag);
    }

    public TagResponse update(TagRequest request, Long id) {
        Tag tag = repository.findById(id)
                .orElseThrow(() -> new TagNotFoundException("Tag not found"));
        tag.setName(request.getName());
        return mapper.toResponse(repository.save(tag));
    }

    public void delete(Long id) {
        Tag tag = repository.findById(id)
                .orElseThrow(() -> new TagNotFoundException("Tag not found"));
        repository.delete(tag);
    }
}
