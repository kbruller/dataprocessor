package com.example.dataprocessor.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.util.Set;

public record DataRequest(
        @NotBlank(message = "Name must not be empty")
        String name,

        @Positive(message = "Value must be positive")
        double value,

        Long categoryId,

        Set<Long> tagIds

) {
}