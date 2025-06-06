package com.olieniev.bookingapp.dto.accommodation;

import com.olieniev.bookingapp.model.Accommodation;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.Set;

public record UpdateAccommodationRequestDto(
        Accommodation.Type type,
        String address,
        String size,
        Set<String> amenities,
        @PositiveOrZero
        BigDecimal dailyRate,
        @PositiveOrZero
        Integer availability
) {
}
