package com.example.dataprocessor.mapper;

import com.example.dataprocessor.entity.CategoryEntity;
import com.example.dataprocessor.model.dto.CategoryRequest;
import com.example.dataprocessor.model.dto.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    CategoryEntity toEntity(CategoryRequest request);

    CategoryResponse toResponse(CategoryEntity entity);
}