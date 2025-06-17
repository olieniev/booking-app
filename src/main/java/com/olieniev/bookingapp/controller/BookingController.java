package com.olieniev.bookingapp.controller;

import com.olieniev.bookingapp.dto.booking.BookingDto;
import com.olieniev.bookingapp.dto.booking.CreateBookingRequestDto;
import com.olieniev.bookingapp.dto.booking.DetailedBookingDto;
import com.olieniev.bookingapp.dto.booking.UpdateBookingRequestDto;
import com.olieniev.bookingapp.model.Booking;
import com.olieniev.bookingapp.model.User;
import com.olieniev.bookingapp.service.booking.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Tag(name = "Controller for Booking class",
        description = "All methods of Booking controller")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @Operation(summary = "Create a booking method",
            description = "Creates a booking with given parameters")
    @PreAuthorize("hasAnyRole('MANAGER','USER')")
    public BookingDto createBooking(Authentication authentication,
                                    @RequestBody @Valid CreateBookingRequestDto requestDto) {
        return bookingService.save((User) authentication.getPrincipal(), requestDto);
    }

    @GetMapping()
    @Operation(summary = "Get bookings by user id and status method",
            description = "Retrieves bookings based on provided user id and status")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<BookingDto> getByUserIdAndStatus(
            @RequestParam(name = "user_id", required = false) Long userId,
            @RequestParam(defaultValue = "PENDING") Booking.Status status,
            Pageable pageable
    ) {
        return bookingService.findAllByUserIdAndStatus(userId, status,pageable);
    }

    @GetMapping("/my")
    @Operation(summary = "Get all bookings of a user",
            description = "Retrieves all bookings of currently logged in user")
    @PreAuthorize("hasAnyRole('MANAGER','USER')")
    public Page<BookingDto> getBookingsOfUser(Authentication authentication, Pageable pageable) {
        return bookingService.findAllByUserId((User) authentication.getPrincipal(), pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get booking by id",
            description = "Retrieves a booking based on provided id")
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    public DetailedBookingDto getDetailedBookingById(
            Authentication authentication, @PathVariable Long id
    ) {
        return bookingService.getByBookingId((User) authentication.getPrincipal(), id);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update booking by id",
            description = "Updates booking by id and given parameter")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','USER')")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto updateBookingById(
            Authentication authentication,
            @PathVariable Long id,
            @RequestBody UpdateBookingRequestDto requestDto
    ) {
        return bookingService.updateByBookingId(
            (User) authentication.getPrincipal(),
            id, requestDto
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete booking by id",
            description = "Deletes booking by given id")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBookingById(
            @PathVariable Long id
    ) {
        bookingService.deleteByBookingId(id);
    }
}
