package ua.com.lab.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ua.com.lab.core.e.DeleteNotAllowedException;
import ua.com.lab.core.e.NotFoundException;
import ua.com.lab.core.e.ValidationException;
import ua.com.lab.web.dto.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest; // Spring Boot 3 використовує jakarta
import java.time.Instant;

@RestControllerAdvice // Ловить помилки з усіх @RestController
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex, HttpServletRequest req) {
        ErrorResponse error = new ErrorResponse(Instant.now(), 404, "Not Found", ex.getMessage(), req.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ValidationException ex, HttpServletRequest req) {
        ErrorResponse error = new ErrorResponse(Instant.now(), 400, "Bad Request", ex.getMessage(), req.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DeleteNotAllowedException.class)
    public ResponseEntity<ErrorResponse> handleDeleteNotAllowed(DeleteNotAllowedException ex, HttpServletRequest req) {
        ErrorResponse error = new ErrorResponse(Instant.now(), 409, "Conflict", ex.getMessage(), req.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest req) {
        // Обробка всіх інших помилок 500
        ErrorResponse error = new ErrorResponse(Instant.now(), 500, "Internal Server Error", ex.getMessage(), req.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}