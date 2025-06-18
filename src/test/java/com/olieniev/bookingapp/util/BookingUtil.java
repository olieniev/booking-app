package com.olieniev.bookingapp.util;

import com.olieniev.bookingapp.dto.accommodation.AccommodationDto;
import com.olieniev.bookingapp.dto.booking.BookingDto;
import com.olieniev.bookingapp.dto.booking.CreateBookingRequestDto;
import com.olieniev.bookingapp.dto.booking.DetailedBookingDto;
import com.olieniev.bookingapp.dto.user.UserDto;
import com.olieniev.bookingapp.model.Accommodation;
import com.olieniev.bookingapp.model.Booking;
import com.olieniev.bookingapp.model.Role;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Set;

public class BookingUtil {
    public static CreateBookingRequestDto createBookingRequestDto() {
        return new CreateBookingRequestDto(
            LocalDate.of(2025, Month.JUNE, 20),
            LocalDate.of(2025, Month.JUNE, 30),
            10L
        );
    }

    public static BookingDto createBookingDto() {
        return new BookingDto(
            1L,
            LocalDate.of(2025, Month.JUNE, 20),
            LocalDate.of(2025, Month.JUNE, 30),
            30L,
            10L,
            Booking.Status.PENDING
        );
    }

    public static Booking getBooking() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setCheckInDate(LocalDate.of(2025, Month.JUNE, 20));
        booking.setCheckOutDate(LocalDate.of(2025, Month.JUNE, 30));
        return booking;
    }

    public static List<Booking> getListOfBookings() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setCheckInDate(LocalDate.of(2025, Month.JUNE, 20));
        booking.setCheckOutDate(LocalDate.of(2025, Month.JUNE, 30));
        return List.of(booking);
    }

    public static List<BookingDto> getBookingsById() {
        return List.of(new BookingDto(
            12L,
            LocalDate.of(2025, Month.NOVEMBER, 2),
            LocalDate.of(2025, Month.NOVEMBER, 8),
            40L,
            10L,
            Booking.Status.CONFIRMED
        ));
    }

    public static DetailedBookingDto getDetailedDto() {
        return new DetailedBookingDto(
            13L,
            LocalDate.of(2025, Month.OCTOBER, 2),
            LocalDate.of(2025, Month.OCTOBER, 8),
                new UserDto(
                20L,
                "user20@email.com",
                "usertwenty",
                "usertwenty",
                Role.ROLE_MANAGER
            ),
                new AccommodationDto(
                10L,
                Accommodation.Type.HOTEL,
                "1063, BUDAPEST",
                "two-person suite",
                Set.of("amenity one", "amenity two"),
                BigDecimal.valueOf(45.99),
                35
            ),
            Booking.Status.CANCELED
        );
    }

    public static Booking getBookingForDetailedDto() {
        Booking booking = new Booking();
        booking.setId(13L);
        booking.setCheckInDate(LocalDate.of(2025, Month.OCTOBER, 2));
        booking.setCheckOutDate(LocalDate.of(2025, Month.OCTOBER, 8));
        booking.setStatus(Booking.Status.CANCELED);
        return booking;
    }

    public static BookingDto getUpdatedBooking() {
        return new BookingDto(
            11L,
            LocalDate.of(2025, Month.JUNE, 6),
            LocalDate.of(2025, Month.JUNE, 13),
            20L,
            10L,
            Booking.Status.CANCELED
        );
    }
}
