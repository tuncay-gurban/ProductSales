package com.example.test2.service;

import com.example.test2.dto.TagRequest;
import com.example.test2.dto.TagResponse;
import com.example.test2.mapper.TagMapper;
import com.example.test2.repository.TagReppository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TagService {
    private final TagReppository reppository;
    private final TagMapper mapper;

    public TagResponse create(TagRequest request) {
        return mapper.toResponse(reppository.save(mapper.toEntity(request)));
    }

    public List<TagResponse> getAll() {
        return mapper.toResponseList(reppository.findAll());
    }
}
