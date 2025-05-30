package com.olieniev.bookingapp.dto.accommodation;

import com.olieniev.bookingapp.model.Accommodation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.Set;

public record CreateAccommodationRequestDto(
        @NotNull
        Accommodation.Type type,
        @NotBlank
        String address,
        @NotBlank
        String size,
        @NotEmpty
        Set<@NotBlank String> amenities,
        @NotNull
        @PositiveOrZero
        BigDecimal dailyRate,
        @NotNull
        @PositiveOrZero
        Integer availability
) {}
