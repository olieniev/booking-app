package com.olieniev.bookingapp.exception;

public class UnauthorisedRoleChangeException extends RuntimeException {
    public UnauthorisedRoleChangeException(String message) {
        super(message);
    }
}
