package com.olieniev.bookingapp.exception;

public class BookingAccessDeniedException extends RuntimeException {
    public BookingAccessDeniedException(String message) {
        super(message);
    }
}
