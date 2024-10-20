package org.example.exception;

public class NonexistentCurrencyException extends RuntimeException {
    public NonexistentCurrencyException(String message) {
        super(message);
    }
}