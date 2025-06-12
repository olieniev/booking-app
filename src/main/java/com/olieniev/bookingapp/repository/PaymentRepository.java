package com.olieniev.bookingapp.repository;

import com.olieniev.bookingapp.model.Payment;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @EntityGraph(attributePaths = {"booking.user"})
    Page<Payment> findByBookingUserId(Long id, Pageable pageable);

    Optional<Payment> findBySessionId(String id);
}
