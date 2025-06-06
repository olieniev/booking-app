package com.olieniev.bookingapp.exception;

public class UnavailableAccommodationException extends RuntimeException {
    public UnavailableAccommodationException(String message) {
        super(message);
    }
}
