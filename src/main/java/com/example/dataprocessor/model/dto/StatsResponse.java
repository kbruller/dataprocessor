package com.example.dataprocessor.model.dto;

public record StatsResponse(
        int count,
        double min,
        double max,
        double average
) {
}