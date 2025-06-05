package com.olieniev.bookingapp.service.booking;

import com.olieniev.bookingapp.dto.booking.BookingDto;
import com.olieniev.bookingapp.dto.booking.CreateBookingRequestDto;
import com.olieniev.bookingapp.dto.booking.DetailedBookingDto;
import com.olieniev.bookingapp.dto.booking.UpdateBookingRequestDto;
import com.olieniev.bookingapp.exception.BookingAccessDeniedException;
import com.olieniev.bookingapp.exception.EntityNotFoundException;
import com.olieniev.bookingapp.exception.UnavailableAccommodationException;
import com.olieniev.bookingapp.mapper.BookingMapper;
import com.olieniev.bookingapp.model.Accommodation;
import com.olieniev.bookingapp.model.Booking;
import com.olieniev.bookingapp.model.Role;
import com.olieniev.bookingapp.model.User;
import com.olieniev.bookingapp.repository.AccommodationRepository;
import com.olieniev.bookingapp.repository.BookingRepository;
import com.olieniev.bookingapp.service.notification.NotificationService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {
    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;
    private final AccommodationRepository accommodationRepository;
    private final NotificationService notificationService;

    @Override
    public BookingDto save(User user, CreateBookingRequestDto requestDto) {
        isDateLegit(requestDto.checkInDate(), requestDto.checkOutDate());
        Accommodation accommodation = accommodationRepository
                .findById(requestDto.accommodationId())
                .orElseThrow(() -> new EntityNotFoundException(
                "Can't find an accommodation by id: " + requestDto.accommodationId()
            ));
        if (accommodation.getAvailability() == 0) {
            throw new UnavailableAccommodationException("Requested accommodation is unavailable!");
        }
        List<Booking> existingBookings = bookingRepository
                .findByUserIdAndAccommodationId(user.getId(), requestDto.accommodationId());
        for (Booking booking : existingBookings) {
            if (isBookingActive(booking) && isDateOverlap(
                    requestDto.checkInDate(),
                    requestDto.checkOutDate(),
                    booking.getCheckInDate(),
                    booking.getCheckOutDate())) {
                throw new BookingAccessDeniedException(
                    "You already have a booking for these dates. Please refer to booking id: "
                        + booking.getId()
                );
            }
        }
        Booking booking = bookingMapper.toModel(requestDto);
        booking.setUser(user);
        booking.setAccommodation(accommodation);
        BookingDto dto = bookingMapper.toDto(bookingRepository.save(booking));
        notificationService.notify(createNotification(dto));
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookingDto> findAllByUserIdAndStatus(
            Long userId,
            Booking.Status status,
            Pageable pageable
    ) {
        if (userId == null) {
            return bookingRepository.findByStatus(status, pageable)
                .map(bookingMapper::toDto);
        }
        return bookingRepository.findByUserIdAndStatus(userId, status, pageable)
            .map(bookingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookingDto> findAllByUserId(User user, Pageable pageable) {
        return bookingRepository.findByUserId(user.getId(), pageable).map(bookingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public DetailedBookingDto getByBookingId(User user, Long bookingId) {
        Booking booking = getBookingIfAccessible(user, bookingId);
        return bookingMapper.toDetailedDto(booking);
    }

    @Override
    public BookingDto updateByBookingId(
            User user, Long bookingId, UpdateBookingRequestDto requestDto
    ) {
        Booking booking = getBookingIfAccessible(user, bookingId);
        if (user.getRole() == Role.ROLE_USER
                && (!(booking.getStatus() == Booking.Status.PENDING
                    && requestDto.status() == Booking.Status.CANCELED))) {
            throw new BookingAccessDeniedException(
                "You are not authorized to change the status to " + requestDto.status()
            );
        }
        if (requestDto.status() == booking.getStatus()) {
            throw new BookingAccessDeniedException(
                "Booking already has a status: " + booking.getStatus()
            );
        }
        Booking.Status oldStatus = booking.getStatus();
        booking.setStatus(requestDto.status());
        booking = bookingRepository.save(booking);
        changeAvailability(oldStatus, requestDto.status(), booking.getAccommodation());
        notificationService.notify(updateNotification(
                bookingId,user, oldStatus, requestDto.status()
        ));
        return bookingMapper.toDto(booking);
    }

    @Override
    public void deleteByBookingId(User user, Long bookingId) {
        bookingRepository.deleteById(bookingId);
    }

    private boolean isOwnerOrUser(User user, Booking booking) {
        return booking.getUser().getId().equals(user.getId())
            || booking.getAccommodation().getOwner().getId().equals(user.getId());
    }

    private Booking getBookingIfAccessible(User user, Long bookingId) {
        return switch (user.getRole()) {
            case ROLE_ADMIN -> bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException(
                    "Can't find a booking with id: " + bookingId)
                );
            case ROLE_MANAGER -> {
                Booking booking = bookingRepository.findById(bookingId)
                        .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find booking by id: " + bookingId
                    ));
                if (!isOwnerOrUser(user, booking)) {
                    throw new BookingAccessDeniedException(
                        "You do not have an access to this booking"
                    );
                }
                yield booking;
            }
            default -> bookingRepository.findByIdAndUserId(bookingId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                    "Can't find a booking with id: " + bookingId + " and user id: " + user.getId()
                ));
        };
    }

    private void isDateLegit(
            LocalDate start1, LocalDate end1) {
        if (end1.isBefore(start1)) {
            throw new IllegalArgumentException("Booking start date cannot be after end date!");
        }
        if (start1.isEqual(end1)) {
            throw new IllegalArgumentException("Minimum booking duration is one night!");
        }
    }

    private boolean isDateOverlap(
            LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        return (start1.isBefore(end2) && start2.isBefore(end1));
    }

    private boolean isBookingActive(Booking booking) {
        return booking.getStatus() == Booking.Status.PENDING
            || booking.getStatus() == Booking.Status.CONFIRMED;
    }

    private String createNotification(BookingDto dto) {
        return """
            New booking has been created! 🗓️
            Please, see booking details below:
            %s
            """.formatted(dto);
    }

    private String updateNotification(
            Long id,
            User user,
            Booking.Status oldStatus,
            Booking.Status newStatus) {
        return """
            Booking with id %d has been updated by a user with id %d and a role - %s! ✏️
            Old status: %s
            New status: %s
            """.formatted(id, user.getId(), user.getRole(), oldStatus, newStatus);
    }

    private void changeAvailability(
            Booking.Status oldStatus, Booking.Status newStatus, Accommodation accommodation
    ) {
        if (oldStatus == Booking.Status.PENDING && newStatus == Booking.Status.CONFIRMED) {
            accommodation.setAvailability(accommodation.getAvailability() - 1);
        }
        if (oldStatus == Booking.Status.CONFIRMED
                && (newStatus == Booking.Status.CANCELED || newStatus == Booking.Status.EXPIRED)) {
            accommodation.setAvailability(accommodation.getAvailability() + 1);
        }
        accommodationRepository.save(accommodation);
    }
}
