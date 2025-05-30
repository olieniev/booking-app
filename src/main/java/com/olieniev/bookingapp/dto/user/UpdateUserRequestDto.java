package com.olieniev.bookingapp.dto.user;

public record UpdateUserRequestDto(
        String email,
        String firstName,
        String lastName
) {
}
