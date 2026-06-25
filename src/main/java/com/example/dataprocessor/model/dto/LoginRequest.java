package com.example.dataprocessor.model.dto;

public record LoginRequest(
        String username,
        String password
) {
}