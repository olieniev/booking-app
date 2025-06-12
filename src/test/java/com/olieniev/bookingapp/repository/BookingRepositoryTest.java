package com.olieniev.bookingapp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.olieniev.bookingapp.model.Booking;
import com.olieniev.bookingapp.model.User;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookingRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;

    @Test
    @DisplayName("""
        Find bookings by user id and status returns needed bookings
            """)
    @Sql(scripts = {
        "classpath:database/users/insert-three-users.sql",
        "classpath:database/accommodations/insert-single-accommodation.sql",
        "classpath:database/bookings/insert-three-bookings.sql"
    })
    @Sql(scripts = {
        "classpath:database/bookings/delete-from-bookings.sql",
        "classpath:database/accommodations/delete-from-accommodations.sql",
        "classpath:database/users/delete-from-users.sql",
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAll_bookingsByUserIdAndStatus_returnsExpectedBooking() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Booking> bookingsPage = bookingRepository.findByUserIdAndStatus(
                40L, Booking.Status.CONFIRMED, pageable
        );
        List<Long> actualUserId = bookingsPage.map(Booking::getUser).map(User::getId).toList();
        List<Booking.Status> actualStatus = bookingsPage.map(Booking::getStatus).toList();
        List<Long> expectedUserId = List.of(40L);
        List<Booking.Status> expectedStatus = List.of(Booking.Status.CONFIRMED);
        assertEquals(expectedStatus, actualStatus);
        assertEquals(expectedUserId, actualUserId);
    }

    @Test
    @DisplayName("""
        Find bookings by user id returns needed bookings
            """)
    @Sql(scripts = {
        "classpath:database/users/insert-three-users.sql",
        "classpath:database/accommodations/insert-single-accommodation.sql",
        "classpath:database/bookings/insert-three-bookings.sql"
    })
    @Sql(scripts = {
        "classpath:database/bookings/delete-from-bookings.sql",
        "classpath:database/accommodations/delete-from-accommodations.sql",
        "classpath:database/users/delete-from-users.sql",
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAll_bookingsByUserId_returnsExpectedBookings() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Booking> bookingsPage = bookingRepository.findByUserId(20L, pageable);
        List<Long> actualIds = bookingsPage.map(Booking::getId).toList();
        List<Long> expectedIds = List.of(11L, 13L);
        assertEquals(expectedIds, actualIds);
    }

    @Test
    @DisplayName("""
        Find booking by booking id returns needed booking
            """)
    @Sql(scripts = {
        "classpath:database/users/insert-three-users.sql",
        "classpath:database/accommodations/insert-single-accommodation.sql",
        "classpath:database/bookings/insert-three-bookings.sql"
    })
    @Sql(scripts = {
        "classpath:database/bookings/delete-from-bookings.sql",
        "classpath:database/accommodations/delete-from-accommodations.sql",
        "classpath:database/users/delete-from-users.sql",
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void find_bookingById_returnsExpectedBooking() {
        Booking booking = bookingRepository.findById(13L).get();
        Long actual = booking.getId();
        Long expected = 13L;
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
        Find booking by status and date less than returns needed booking
            """)
    @Sql(scripts = {
        "classpath:database/users/insert-three-users.sql",
        "classpath:database/accommodations/insert-single-accommodation.sql",
        "classpath:database/bookings/insert-three-bookings.sql"
    })
    @Sql(scripts = {
        "classpath:database/bookings/delete-from-bookings.sql",
        "classpath:database/accommodations/delete-from-accommodations.sql",
        "classpath:database/users/delete-from-users.sql",
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAll_bookingByStatusAndDteLessThanOrEqual_returnsExpectedBooking() {
        List<Booking.Status> statuses = List.of(
                Booking.Status.CONFIRMED, Booking.Status.PENDING
        );
        LocalDate tomorrow = LocalDate.now(ZoneId.of("Europe/Budapest")).plusDays(1);
        List<Booking> bookings = bookingRepository
                .findAllByStatusInAndCheckOutDateLessThanEqual(statuses, tomorrow);
        Long actual = bookings.get(0).getId();
        Long expected = 11L;
        assertEquals(1L, bookings.size());
        assertEquals(expected, actual);
    }
}
