package com.example.dataprocessor.exception;

public class InvalidPageSizeException extends RuntimeException {
    public InvalidPageSizeException(int maxSize) {

        super("Page size must not be greater than " + maxSize);
    }
}
