package com.olieniev.bookingapp.util;

import com.olieniev.bookingapp.dto.payment.PaymentDto;
import com.olieniev.bookingapp.model.Payment;
import java.math.BigDecimal;
import java.util.List;

public class PaymentUtil {
    public static List<PaymentDto> getListOfPayments() {
        return List.of(
            new PaymentDto(
                11L,
                Payment.Status.PENDING,
                "booking 11 session id",
                BigDecimal.valueOf(10.99)),
            new PaymentDto(
                13L,
                Payment.Status.PAID,
                "booking 13 session id",
                BigDecimal.valueOf(12.99))
        );
    }
}
