package com.olieniev.bookingapp.dto.payment;

import jakarta.validation.constraints.NotNull;

public record CreatePaymentRequestDto(
        @NotNull
        Long bookingId
) {
}
