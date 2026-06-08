package com.example.dataprocessor.exception;

public class CategoryInUseException extends RuntimeException {

    public CategoryInUseException(Long id) {
        super("Category cannot be deleted because it is used by data records. Category id: " + id);
    }
}
