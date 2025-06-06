package com.olieniev.bookingapp.dto.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CreateBookingRequestDto(
        @NotNull
        @JsonFormat(pattern = "dd-MM-yyyy")
        @FutureOrPresent
        LocalDate checkInDate,
        @NotNull
        @JsonFormat(pattern = "dd-MM-yyyy")
        @FutureOrPresent
        LocalDate checkOutDate,
        @NotNull
        Long accommodationId
) {
}
