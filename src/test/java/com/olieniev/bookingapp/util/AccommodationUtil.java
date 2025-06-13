package com.olieniev.bookingapp.util;

import com.olieniev.bookingapp.dto.accommodation.AccommodationDto;
import com.olieniev.bookingapp.dto.accommodation.CreateAccommodationRequestDto;
import com.olieniev.bookingapp.model.Accommodation;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class AccommodationUtil {
    public static CreateAccommodationRequestDto createAccommodationRequestDto() {
        return new CreateAccommodationRequestDto(
            Accommodation.Type.VACATION_HOME,
            "the best address",
            "three-suite house",
            Set.of("the best amenities"),
            BigDecimal.valueOf(120.00),
            6
        );
    }

    public static AccommodationDto createAccommodationDto() {
        return new AccommodationDto(
            1L,
            Accommodation.Type.VACATION_HOME,
            "the best address",
            "three-suite house",
            Set.of("the best amenities"),
            BigDecimal.valueOf(120.00),
            6
        );
    }

    public static AccommodationDto getAlternativeDto() {
        return new AccommodationDto(
            22L,
            Accommodation.Type.CONDO,
            "1, KYIV",
            "three-person suite",
            Set.of("THIRD ONE"),
            BigDecimal.valueOf(51.99),
            1);
    }

    public static List<AccommodationDto> getListOfDto() {
        return List.of(
            new AccommodationDto(
                20L,
                Accommodation.Type.CONDO,
                "1063, BUDAPEST",
                "ONE-person suite",
                Set.of("FIRST ONE"),
                BigDecimal.valueOf(49.99),
                1),
            new AccommodationDto(
                21L,
                Accommodation.Type.CONDO,
                "65069, ODESA",
                "TWO-person suite",
                Set.of("SECOND ONE"),
                BigDecimal.valueOf(50.99),
                1),
            new AccommodationDto(
                22L,
                Accommodation.Type.CONDO,
                "1, KYIV",
                "three-person suite",
                Set.of("THIRD ONE"),
                BigDecimal.valueOf(51.99),
                1
            )
        );
    }
}
