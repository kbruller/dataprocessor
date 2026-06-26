package com.example.dataprocessor.exception;

import com.example.dataprocessor.model.dto.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(TagInUseException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorResponse handleTagInUse(TagInUseException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleDataNotFound(DataNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(InvalidSortFieldException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleInvalidSortField(
            InvalidSortFieldException ex
    ) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(InvalidPageSizeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleInvalidPageSize(
            InvalidPageSizeException ex
    ) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleCategoryNotFound(CategoryNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(CategoryInUseException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorResponse handleCategoryInUse(CategoryInUseException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(TagNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleTagNotFound(
            TagNotFoundException ex
    ) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleValidationErrors(
            MethodArgumentNotValidException ex
    ) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), errors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        assert ex.getRequiredType() != null;
        String message = String.format(
                "The value of the '%s' parameter ('%s') is of the wrong type. Expected type: %s",
                ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

        return buildErrorResponse(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleMalformedJson(HttpMessageNotReadableException ex) {
        // This is triggered if the JSON sent by the client contains a syntax error
        // (e.g., a missing comma or curly brace)
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorResponse handleGeneralException(Exception ex) {
        log.error("An unexpected internal server error occurred.", ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    private ApiErrorResponse buildErrorResponse(HttpStatus status, String message) {
        return buildErrorResponse(status, message, null);
    }

    private ApiErrorResponse buildErrorResponse(HttpStatus status, String message, Map<String, String> errors) {
        return new ApiErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                errors
        );
    }
}