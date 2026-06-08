package com.example.dataprocessor.exception;

public class DataNotFoundException extends RuntimeException {

    public DataNotFoundException(Long id) {
        super("Data not found with id: " + id);
    }
}