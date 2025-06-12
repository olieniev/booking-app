package com.olieniev.bookingapp.dto.payment;

public record CreatePaymentResponseDto(
        String url,
        String sessionId
) {
}
