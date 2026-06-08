package com.example.dataprocessor.model.dto;

import java.util.Set;

public record DataResponse(
        Long id,
        String name,
        double value,
        Long categoryId,
        String categoryName,
        Set<String> tags
) {
}