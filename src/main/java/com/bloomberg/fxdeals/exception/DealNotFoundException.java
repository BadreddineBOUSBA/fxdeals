package com.bloomberg.fxdeals.exception;

public class DealNotFoundException extends RuntimeException {

    public DealNotFoundException(String message) {
        super(message);
    }

    public DealNotFoundException(Long id) {
        super("Deal not found with ID: " + id);
    }
}