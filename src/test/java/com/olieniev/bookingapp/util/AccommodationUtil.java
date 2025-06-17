package com.olieniev.bookingapp.util;

import com.olieniev.bookingapp.dto.accommodation.AccommodationDto;
import com.olieniev.bookingapp.dto.accommodation.CreateAccommodationRequestDto;
import com.olieniev.bookingapp.model.Accommodation;
import com.olieniev.bookingapp.model.User;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class AccommodationUtil {
    public static Accommodation getAccommodation() {
        Accommodation accommodation = new Accommodation();
        accommodation.setId(1L);
        accommodation.setType(Accommodation.Type.VACATION_HOME);
        accommodation.setAddress("the best address");
        accommodation.setSize("three-suite house");
        accommodation.setAmenities(Set.of("the best amenities"));
        accommodation.setDailyRate(BigDecimal.valueOf(120.00));
        accommodation.setAvailability(6);
        accommodation.setOwner(new User());
        return accommodation;
    }

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

    public static List<Accommodation> getListOfAccommodations() {
        Accommodation accommodation1 = new Accommodation();
        accommodation1.setId(20L);
        accommodation1.setType(Accommodation.Type.CONDO);
        accommodation1.setAddress("1063, BUDAPEST");
        accommodation1.setSize("ONE-person suite");
        accommodation1.setAmenities(Set.of("FIRST ONE"));
        accommodation1.setDailyRate(BigDecimal.valueOf(49.99));
        accommodation1.setAvailability(1);

        Accommodation accommodation2 = new Accommodation();
        accommodation2.setId(21L);
        accommodation2.setType(Accommodation.Type.CONDO);
        accommodation2.setAddress("65069, ODESA");
        accommodation2.setSize("TWO-person suite");
        accommodation2.setAmenities(Set.of("SECOND ONE"));
        accommodation2.setDailyRate(BigDecimal.valueOf(50.99));
        accommodation2.setAvailability(1);

        Accommodation accommodation3 = new Accommodation();
        accommodation3.setId(22L);
        accommodation3.setType(Accommodation.Type.CONDO);
        accommodation3.setAddress("1, KYIV");
        accommodation3.setSize("three-person suite");
        accommodation3.setAmenities(Set.of("THIRD ONE"));
        accommodation3.setDailyRate(BigDecimal.valueOf(51.99));
        accommodation3.setAvailability(1);

        return List.of(accommodation1, accommodation2, accommodation3);
    }
}
