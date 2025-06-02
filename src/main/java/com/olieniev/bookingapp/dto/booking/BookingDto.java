package com.olieniev.bookingapp.dto.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.olieniev.bookingapp.model.Booking;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record BookingDto(
        Long id,
        @JsonFormat(pattern = "dd-MM-yyyy")
        LocalDate checkInDate,
        @JsonFormat(pattern = "dd-MM-yyyy")
        LocalDate checkOutDate,
        Long userId,
        Long accommodationId,
        @NotNull
        Booking.Status status
) {
}
