package com.olieniev.bookingapp.dto.user;

import com.olieniev.bookingapp.model.Role;

public record UserDto(
        Long id,
        String email,
        String firstName,
        String lastName,
        Role role
) {
}
