package com.olieniev.bookingapp.exception;

public class StripeSessionException extends RuntimeException {
    public StripeSessionException(String message) {
        super(message);
    }
}
