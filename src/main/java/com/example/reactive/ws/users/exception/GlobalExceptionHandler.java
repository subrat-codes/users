package com.example.reactive.ws.users.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateKeyException.class)
    public Mono<ErrorResponse> handleDuplicateKeyException(DuplicateKeyException exception) {
        final var errorResponse = ErrorResponse.builder(exception, HttpStatus.CONFLICT,
                exception.getMessage()).build();
        return Mono.just(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public Mono<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        final var errorResponse = ErrorResponse.builder(exception, HttpStatus.BAD_REQUEST,
                exception.getMessage()).build();
        return Mono.just(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public Mono<ErrorResponse> handleGeneralException(Exception exception) {
        final var errorResponse = ErrorResponse.builder(exception, HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage()).build();
        return Mono.just(errorResponse);
    }
}
