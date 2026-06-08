package com.example.dataprocessor.model.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
        @NotBlank
        String name
) {
}
