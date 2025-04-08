package ru.practicum.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ErrorHandler {
    /*@ExceptionHandler
    public ErrorResponse handleAnyException(Exception ex) {
        return new ErrorResponse(ex.getStackTrace(), ex.getMessage(), )
    }*/

    public record ErrorResponse(List<String> errors, String message, String reason, String status, String timestamp) {}
}
