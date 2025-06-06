package com.olieniev.bookingapp.exception;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomGlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationExceptions(
            MethodArgumentNotValidException ex
    ) {
        List<String> errors = ex.getBindingResult()
                .getAllErrors().stream()
                .map(objectError -> {
                    String field = ((FieldError)objectError).getField();
                    String message = objectError.getDefaultMessage();
                    return "Field '" + field + "' " + message;
                })
                .toList();
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookingAccessDeniedException.class)
    public ResponseEntity<String> handleBookingAccessDeniedExceptions(
            BookingAccessDeniedException ex
    ) {
        return new ResponseEntity<>(ex.getMessage(),
            HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundExceptions(
            EntityNotFoundException ex
    ) {
        return new ResponseEntity<>(ex.getMessage(),
            HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OwnerNotAuthorisedException.class)
    public ResponseEntity<String> handleOwnerNotAuthorisedExceptions(
            OwnerNotAuthorisedException ex
    ) {
        return new ResponseEntity<>(ex.getMessage(),
            HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<String> handleRegistrationExceptions(
            RegistrationException ex
    ) {
        return new ResponseEntity<>(ex.getMessage(),
            HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnauthorisedRoleChangeException.class)
    public ResponseEntity<String> handleUnauthorisedRoleChangeExceptions(
            UnauthorisedRoleChangeException ex
    ) {
        return new ResponseEntity<>(ex.getMessage(),
            HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UnavailableAccommodationException.class)
    public ResponseEntity<String> handleUnavailableAccommodationExceptions(
            UnavailableAccommodationException ex
    ) {
        return new ResponseEntity<>(ex.getMessage(),
            HttpStatus.CONFLICT);
    }
}
