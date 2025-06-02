package com.olieniev.bookingapp.dto.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.olieniev.bookingapp.dto.accommodation.AccommodationDto;
import com.olieniev.bookingapp.dto.user.UserDto;
import com.olieniev.bookingapp.model.Booking;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record DetailedBookingDto(
        Long id,
        @JsonFormat(pattern = "dd-MM-yyyy")
        LocalDate checkInDate,
        @JsonFormat(pattern = "dd-MM-yyyy")
        LocalDate checkOutDate,
        UserDto user,
        AccommodationDto accommodation,
        @NotNull
        Booking.Status status
) {
}
