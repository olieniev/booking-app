package com.olieniev.bookingapp.repository;

import com.olieniev.bookingapp.model.Booking;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findByUserIdAndStatus(Long userId, Booking.Status status, Pageable pageable);

    Page<Booking> findByUserId(Long userId, Pageable pageable);

    Optional<Booking> findByIdAndUserId(Long bookingId, Long id);

    @EntityGraph(attributePaths = {"user","accommodation","accommodation.owner"})
    Optional<Booking> findById(Long bookingId);

    Page<Booking> findByStatus(Booking.Status status, Pageable pageable);

    List<Booking> findByUserIdAndAccommodationId(Long userId, Long accommodationId);

    @EntityGraph(attributePaths = {"accommodation"})
    List<Booking> findAllByStatusInAndCheckOutDateLessThanEqual(
            List<Booking.Status> statusesForExpiration, LocalDate tomorrow
    );
}
