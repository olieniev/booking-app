package com.olieniev.bookingapp.service;

import static com.olieniev.bookingapp.util.BookingUtil.getBookingsById;
import static com.olieniev.bookingapp.util.BookingUtil.getListOfBookings;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.olieniev.bookingapp.dto.booking.BookingDto;
import com.olieniev.bookingapp.mapper.BookingMapper;
import com.olieniev.bookingapp.model.Booking;
import com.olieniev.bookingapp.model.User;
import com.olieniev.bookingapp.repository.AccommodationRepository;
import com.olieniev.bookingapp.repository.BookingRepository;
import com.olieniev.bookingapp.service.booking.BookingServiceImpl;
import com.olieniev.bookingapp.service.notification.NotificationService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @Mock
    private BookingMapper bookingMapper;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private AccommodationRepository accommodationRepository;
    @Mock
    private NotificationService notificationService;
    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    @DisplayName("""
        Get accommodation by user id and status returns list of AccommodationDto
            """)
    public void getBookingByUserIdAndStatus_existsInDb_success() {
        Long userId = 12L;
        Booking.Status status = Booking.Status.CONFIRMED;
        Pageable pageable = PageRequest.of(0, 1);

        List<Booking> listOfBookings = getListOfBookings();
        List<BookingDto> expected = getBookingsById();
        Page<Booking> page = new PageImpl<>(
                listOfBookings, pageable, listOfBookings.size()
        );

        when(bookingRepository.findByUserIdAndStatus(userId, status, pageable)).thenReturn(page);
        when(bookingMapper.toDto(any(Booking.class)))
                .thenReturn(expected.get(0));

        Page<BookingDto> actual = bookingService.findAllByUserIdAndStatus(userId, status, pageable);

        assertEquals(expected.size(), actual.getContent().size());
        verify(bookingRepository).findByUserIdAndStatus(userId, status, pageable);
        verify(bookingMapper).toDto(any(Booking.class));
    }

    @Test
    @DisplayName("""
        Get accommodation by user id returns list of AccommodationDto
            """)
    public void getBookingByUserId_existsInDb_success() {
        Long userId = 12L;
        User user = new User();
        user.setId(userId);
        Pageable pageable = PageRequest.of(0, 1);

        List<Booking> listOfBookings = getListOfBookings();
        List<BookingDto> expected = getBookingsById();
        Page<Booking> page = new PageImpl<>(
                listOfBookings, pageable, listOfBookings.size()
        );

        when(bookingRepository.findByUserId(userId, pageable)).thenReturn(page);
        when(bookingMapper.toDto(any(Booking.class))).thenReturn(expected.get(0));

        Page<BookingDto> actual = bookingService.findAllByUserId(user, pageable);

        assertEquals(expected.size(), actual.getContent().size());
        verify(bookingRepository).findByUserId(userId, pageable);
        verify(bookingMapper).toDto(any(Booking.class));
    }

    @Test
    @DisplayName("""
        Delete accommodation by id returns void
            """)
    public void deleteBookingById_existsInDb_returnsVoid() {
        Long id = 1L;
        doNothing().when(bookingRepository).deleteById(id);
        bookingService.deleteByBookingId(id);
        verify(bookingRepository).deleteById(id);
    }
}
