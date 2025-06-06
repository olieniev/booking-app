package com.olieniev.bookingapp.dto.accommodation;

import com.olieniev.bookingapp.model.Accommodation;
import java.math.BigDecimal;
import java.util.Set;

public record AccommodationDto(
        Long id,
        Accommodation.Type type,
        String address,
        String size,
        Set<String> amenities,
        BigDecimal dailyRate,
        Integer availability
) {
    @Override
    public String toString() {
        return """
            Accommodation id: %d
            Type: %s
            Address: %s
            Size: %s
            Amenities: %s
            Daily rate: %s
            Availability: %d
            """.formatted(
            id,
            type,
            address,
            size,
            amenities,
            dailyRate,
            availability
        );
    }
}
