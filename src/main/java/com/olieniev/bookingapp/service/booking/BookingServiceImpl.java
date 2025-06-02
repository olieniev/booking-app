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

    @Override
    public BookingDto save(User user, CreateBookingRequestDto requestDto) {
        Accommodation accommodation = accommodationRepository
                .findById(requestDto.accommodationId())
                .orElseThrow(() -> new EntityNotFoundException(
                "Can't find an accommodation by id: " + requestDto.accommodationId()
            ));
        if (accommodation.getAvailability() == 0) {
            throw new UnavailableAccommodationException("Requested accommodation is unavailable!");
        }
        Booking booking = bookingMapper.toModel(requestDto);
        booking.setUser(user);
        booking.setAccommodation(accommodation);
        return bookingMapper.toDto(bookingRepository.save(booking));
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
        if (user.getRole() == Role.ROLE_USER
                && (!requestDto.status().equals(Booking.Status.CANCELED))) {
            throw new BookingAccessDeniedException(
                "You are not authorized to change the status to " + requestDto.status()
            );
        }
        Booking booking = getBookingIfAccessible(user, bookingId);
        booking.setStatus(requestDto.status());
        return bookingMapper.toDto(bookingRepository.save(booking));
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
}
