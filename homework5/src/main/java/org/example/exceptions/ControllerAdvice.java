package org.example.exceptions;

import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        String errorMessage = e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).findFirst().orElse("Validation failed");
        errors.put("error", errorMessage);
        log.error("Validation error: {}", errors);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException e) {
        Map<String, String> errors = new HashMap<>();
        String errorMessage = getMessageWithoutMethodName(e.getMessage());
        errors.put("error", errorMessage);
        log.error("Constraint violation error: {}", errors);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    private String getMessageWithoutMethodName(String message) {
        int index = message.indexOf(":");
        return message.substring(index + 2);
    }

    @ExceptionHandler({NonexistentCurrencyException.class, RelatedEntityNotFoundException.class, VerificationCodeException.class, UserAlreadyExistsException.class})
    public ResponseEntity<Map<String, String>> handleNonexistentCurrencyException(RuntimeException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", e.getMessage());
        log.error("Bad request error: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler({CurrencyRateNotFoundException.class, EntityNotFoundException.class, NoSuchElementException.class})
    public ResponseEntity<Map<String, String>> handleCurrencyRateNotFoundException(RuntimeException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", e.getMessage());
        log.error("Resource not found error: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<Map<String, String>> handleServiceUnavailableException(ServiceUnavailableException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", e.getMessage());
        log.error("Service unavailable error: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).header("Retry-After", "3600").body(errors);
    }

}