package com.olieniev.bookingapp.dto.booking;

import com.olieniev.bookingapp.model.Booking;
import jakarta.validation.constraints.NotNull;

public record UpdateBookingRequestDto(
        @NotNull
        Booking.Status status
) {
}
