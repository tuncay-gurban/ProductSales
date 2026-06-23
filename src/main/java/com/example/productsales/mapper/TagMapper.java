package com.example.productsales.mapper;

import com.example.productsales.dto.TagRequest;
import com.example.productsales.dto.TagResponse;
import com.example.productsales.entity.Tag;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagMapper {
    public Tag toEntity(TagRequest request);

    public TagResponse toResponse(Tag tag);

    public List<TagResponse> toResponseList(List<Tag> tags);
}
