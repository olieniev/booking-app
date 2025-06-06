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
    @Override
    public String toString() {
        return """
            Booking id: %d
            Check-in date: %s
            Check-out date: %s
            User id: %d
            Accommodation id: %d
            Current status: %s
            """.formatted(
                    id,
                    checkInDate,
                    checkOutDate,
                    userId,
                    accommodationId,
                    status
                );
    }
}
