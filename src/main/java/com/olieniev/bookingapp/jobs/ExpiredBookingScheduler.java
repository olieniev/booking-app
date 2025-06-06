package com.olieniev.bookingapp.jobs;

import com.olieniev.bookingapp.dto.accommodation.AccommodationDto;
import com.olieniev.bookingapp.mapper.AccommodationMapper;
import com.olieniev.bookingapp.model.Accommodation;
import com.olieniev.bookingapp.model.Booking;
import com.olieniev.bookingapp.repository.BookingRepository;
import com.olieniev.bookingapp.service.notification.NotificationService;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ExpiredBookingScheduler {
    private static final List<Booking.Status> STATUSES_FOR_EXPIRATION = List.of(
            Booking.Status.CONFIRMED, Booking.Status.PENDING
    );
    private static final Booking.Status EXPIRED_STATUS = Booking.Status.EXPIRED;
    private static final String NO_EXPIRED_NOTIFICATION = "No expired bookings today!";
    private final BookingRepository bookingRepository;
    private final NotificationService notificationService;
    private final AccommodationMapper accommodationMapper;

    @Transactional
    @Scheduled(cron = "0 59 23 * * *", zone = "Europe/Budapest")
    public void expireOldBookings() {
        LocalDate tomorrow = LocalDate.now(ZoneId.of("Europe/Budapest")).plusDays(1);
        List<Booking> expiredBookings = bookingRepository
                .findAllByStatusInAndCheckOutDateLessThanEqual(STATUSES_FOR_EXPIRATION, tomorrow);
        if (expiredBookings.isEmpty()) {
            notificationService.notify(NO_EXPIRED_NOTIFICATION);
            return;
        }
        expiredBookings.forEach(booking -> {
            Accommodation accommodation = booking.getAccommodation();
            if (booking.getStatus() == Booking.Status.CONFIRMED) {
                accommodation.setAvailability(accommodation.getAvailability() + 1);
            }
            booking.setStatus(EXPIRED_STATUS);
            notificationService.notify(
                    expireNotification(
                        booking.getId(), tomorrow, accommodationMapper.toDto(accommodation)
                    )
            );
        });
    }

    private String expireNotification(Long id, LocalDate dateOfExpiry, AccommodationDto dto) {
        return """
            Expiry notification! 📌
            Booking with id %d is ending on %s.
            It has been assigned an appropriate status: EXPIRED.
            The accommodation is released now and may be used for new bookings!
            See accommodation details below:
            %s
            """.formatted(id, dateOfExpiry, dto);
    }
}
