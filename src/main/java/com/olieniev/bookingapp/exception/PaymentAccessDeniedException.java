package com.olieniev.bookingapp.exception;

public class PaymentAccessDeniedException extends RuntimeException {
    public PaymentAccessDeniedException(String message) {
        super(message);
    }
}
