package com.olieniev.bookingapp.exception;

public class OwnerNotAuthorisedException extends RuntimeException {
    public OwnerNotAuthorisedException(String message) {
        super(message);
    }
}
