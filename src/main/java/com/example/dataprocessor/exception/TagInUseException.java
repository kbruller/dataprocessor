package com.example.dataprocessor.exception;

public class TagInUseException extends RuntimeException {

    public TagInUseException(Long id) {
        super("Tag cannot be deleted because it is used by data records. Tag id: " + id);
    }
}
