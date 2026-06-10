package com.example.dataprocessor.mapper;

import com.example.dataprocessor.entity.TagEntity;
import com.example.dataprocessor.model.dto.TagRequest;
import com.example.dataprocessor.model.dto.TagResponse;
import org.springframework.stereotype.Component;

@Component
public class TagMapperManual
{

    public TagEntity toEntity(TagRequest request) {
        return TagEntity.builder()
                .name(request.name())
                .build();
    }

    public TagResponse toResponse(TagEntity entity) {
        return new TagResponse(
                entity.getId(),
                entity.getName()
        );
    }
}