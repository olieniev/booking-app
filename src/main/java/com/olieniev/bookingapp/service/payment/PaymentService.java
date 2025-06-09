package com.olieniev.bookingapp.service.payment;

import com.olieniev.bookingapp.dto.payment.CreatePaymentRequestDto;
import com.olieniev.bookingapp.dto.payment.CreatePaymentResponseDto;
import com.olieniev.bookingapp.dto.payment.PaymentDto;
import com.olieniev.bookingapp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentService {
    Page<PaymentDto> getPaymentsById(Long id, User user, Pageable pageable);

    CreatePaymentResponseDto createPayment(
            CreatePaymentRequestDto requestDto,
            User user,
            String baseUrl
    );
}
