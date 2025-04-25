package ru.practicum.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
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

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInterruptedException(InterruptedException ex) {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.name(), "Thread was interrupted", ex.getMessage(),
                LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbiddenException(ConditionsNotMetException ex) {
        return new ErrorResponse(HttpStatus.FORBIDDEN.name(),
                "For the requested operation the conditions are not met.", ex.getMessage(),
                LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handlePSQLException(DataIntegrityViolationException ex) {
        return new ErrorResponse(HttpStatus.FORBIDDEN.name(),
                "Integrity constraint has been violated.", ex.getMessage(),
                LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(BadRequestException ex) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.name(),
                "Incorrectly made request.", ex.getMessage(),
                LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalServerException(InternalServerException ex) {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.name(), "The server failed", ex.getMessage(),
                LocalDateTime.now().format(formatter));
    }

    public record ErrorResponse(String status, String reason, String message, String timestamp) {}
}
