package com.example.dataprocessor.model.dto;

public record DataSearchRequest(
        String name,
        Double minValue,
        Double maxValue
) {
}