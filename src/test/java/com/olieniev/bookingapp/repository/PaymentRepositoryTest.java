package com.olieniev.bookingapp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.olieniev.bookingapp.model.Payment;
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
public class PaymentRepositoryTest {
    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    @DisplayName("""
        Find payment by session id returns needed payment
            """)
    @Sql(scripts = {
            "classpath:database/users/insert-three-users.sql",
            "classpath:database/accommodations/insert-single-accommodation.sql",
            "classpath:database/bookings/insert-three-bookings.sql",
            "classpath:database/payments/insert-three-payments.sql",
            })
    @Sql(scripts = {
        "classpath:database/payments/delete-from-payments.sql",
        "classpath:database/bookings/delete-from-bookings.sql",
        "classpath:database/accommodations/delete-from-accommodations.sql",
        "classpath:database/users/delete-from-users.sql",
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void find_paymentBySessionId_returnsExpectedPayment() {
        Long actualId = paymentRepository.findBySessionId("booking 11 session id").get().getId();
        Long expectedId = 11L;
        assertEquals(expectedId, actualId);
    }

    @Test
    @DisplayName("""
        Find payments by booking's user id returns needed payments
            """)
    @Sql(scripts = {
        "classpath:database/users/insert-three-users.sql",
        "classpath:database/accommodations/insert-single-accommodation.sql",
        "classpath:database/bookings/insert-three-bookings.sql",
        "classpath:database/payments/insert-three-payments.sql",
    })
    @Sql(scripts = {
        "classpath:database/payments/delete-from-payments.sql",
        "classpath:database/bookings/delete-from-bookings.sql",
        "classpath:database/accommodations/delete-from-accommodations.sql",
        "classpath:database/users/delete-from-users.sql",
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAll_paymentsByBookingUserId_returnsExpectedPayments() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Payment> paymentsPage = paymentRepository.findByBookingUserId(20L, pageable);
        List<Long> actual = paymentsPage.map(Payment::getId).toList();
        List<Long> expected = List.of(11L, 13L);
        assertEquals(expected, actual);
    }
}
