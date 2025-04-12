package ru.practicum.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
public class ErrorHandler {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException ex) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.name(), "The required object was not found.",
                ex.getMessage(), LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleBadRequestException(ConstraintViolationException ex) {
        HttpStatus status;
        String reason;
        if (ex.getMessage().contains("email")) {
            reason = "Integrity constraint has been violated.";
            status = HttpStatus.CONFLICT;
        } else {
            reason = "Incorrectly made request.";
            status = HttpStatus.BAD_REQUEST;
        }

        ErrorResponse error = new ErrorResponse(status.name(), reason,
                ex.getMessage(), LocalDateTime.now().format(formatter));

        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(ConflictException ex) {
        return new ErrorResponse(HttpStatus.CONFLICT.name(), ex.getReason(), ex.getMessage(),
                LocalDateTime.now().format(formatter));
    }

    public record ErrorResponse(String status, String reason, String message, String timestamp) {}
}
