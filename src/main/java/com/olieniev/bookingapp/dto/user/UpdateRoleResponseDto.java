package com.olieniev.bookingapp.dto.user;

import com.olieniev.bookingapp.model.Role;

public record UpdateRoleResponseDto(
        Long id,
        String email,
        Role role
) {
}
