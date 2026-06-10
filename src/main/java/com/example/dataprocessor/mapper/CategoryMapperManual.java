package com.example.dataprocessor.mapper;

import com.example.dataprocessor.entity.CategoryEntity;
import com.example.dataprocessor.model.dto.CategoryRequest;
import com.example.dataprocessor.model.dto.CategoryResponse;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapperManual {

    public CategoryEntity toEntity(CategoryRequest request) {
        return new CategoryEntity(request.name());
    }

    public CategoryResponse toResponse(CategoryEntity entity) {
        return new CategoryResponse(
                entity.getId(),
                entity.getName()
        );
    }
}