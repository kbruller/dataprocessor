package com.example.dataprocessor.exception;

public class TagNotFoundException extends RuntimeException {

    public TagNotFoundException(Long id) {
        super("Tag not found with id: " + id);
    }
}