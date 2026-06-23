package com.example.test2.mapper;

import com.example.test2.dto.TagRequest;
import com.example.test2.dto.TagResponse;
import com.example.test2.entity.Tag;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagMapper {
    public Tag toEntity(TagRequest request);

    public TagResponse toResponse(Tag tag);

    public List<TagResponse> toResponseList(List<Tag> tags);
}
