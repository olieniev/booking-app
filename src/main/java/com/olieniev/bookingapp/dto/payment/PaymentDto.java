package com.olieniev.bookingapp.dto.payment;

import com.olieniev.bookingapp.model.Payment;
import java.math.BigDecimal;

public record PaymentDto(
        Long id,
        Payment.Status status,
        String sessionId,
        BigDecimal amountToPay
) {
    @Override
    public String toString() {
        return """
            Payment id: %d
            Status: %s
            Session id: %s
            Amount paid (in USD): %s
            """.formatted(id, status, sessionId, amountToPay);
    }
}
