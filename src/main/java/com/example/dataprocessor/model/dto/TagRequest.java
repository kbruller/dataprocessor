package com.example.dataprocessor.model.dto;

import jakarta.validation.constraints.NotBlank;

public record TagRequest(

        @NotBlank
        String name

) {
}