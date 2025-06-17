package com.olieniev.bookingapp.service.booking;

import com.olieniev.bookingapp.dto.booking.BookingDto;
import com.olieniev.bookingapp.dto.booking.CreateBookingRequestDto;
import com.olieniev.bookingapp.dto.booking.DetailedBookingDto;
import com.olieniev.bookingapp.dto.booking.UpdateBookingRequestDto;
import com.olieniev.bookingapp.model.Booking;
import com.olieniev.bookingapp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookingService {
    BookingDto save(User user, CreateBookingRequestDto requestDto);

    Page<BookingDto> findAllByUserIdAndStatus(Long id, Booking.Status status, Pageable pageable);

    Page<BookingDto> findAllByUserId(User user, Pageable pageable);

    DetailedBookingDto getByBookingId(User user, Long bookingId);

    BookingDto updateByBookingId(User user, Long bookingId, UpdateBookingRequestDto requestDto);

    void deleteByBookingId(Long bookingId);
}
