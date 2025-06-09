package com.olieniev.bookingapp.dto.payment;

import com.olieniev.bookingapp.model.Payment;
import java.math.BigDecimal;

public record PaymentDto(
        Long id,
        Payment.Status status,
        String url,
        String sessionId,
        BigDecimal amountToPay
) {
}
