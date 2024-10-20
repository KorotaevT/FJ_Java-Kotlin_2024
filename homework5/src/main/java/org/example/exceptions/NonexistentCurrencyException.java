package org.example.exceptions;

public class NonexistentCurrencyException extends RuntimeException {
    public NonexistentCurrencyException(String message) {
        super(message);
    }
}