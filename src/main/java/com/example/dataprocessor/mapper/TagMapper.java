package com.example.dataprocessor.mapper;

import com.example.dataprocessor.entity.TagEntity;
import com.example.dataprocessor.model.dto.TagRequest;
import com.example.dataprocessor.model.dto.TagResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TagMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataTags", ignore = true)
    TagEntity toEntity(TagRequest request);

    TagResponse toResponse(TagEntity entity);
}