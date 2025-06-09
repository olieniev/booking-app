package com.olieniev.bookingapp.service.payment;

import com.olieniev.bookingapp.dto.payment.CreatePaymentRequestDto;
import com.olieniev.bookingapp.dto.payment.CreatePaymentResponseDto;
import com.olieniev.bookingapp.dto.payment.PaymentDto;
import com.olieniev.bookingapp.exception.PaymentAccessDeniedException;
import com.olieniev.bookingapp.exception.StripeSessionException;
import com.olieniev.bookingapp.mapper.PaymentMapper;
import com.olieniev.bookingapp.model.Booking;
import com.olieniev.bookingapp.model.Payment;
import com.olieniev.bookingapp.model.Role;
import com.olieniev.bookingapp.model.User;
import com.olieniev.bookingapp.repository.BookingRepository;
import com.olieniev.bookingapp.repository.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {
    private static final String SUCCESS_URL = "/success";
    private static final String CANCEL_URL = "/cancel";
    private static final String TOTAL_STRING = "Total amount";
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final BookingRepository bookingRepository;
    @Value("${stripe.api.token}")
    private String stripeApiKey;

    @Override
    public Page<PaymentDto> getPaymentsById(Long id, User user, Pageable pageable) {
        if (user.getRole() == Role.ROLE_ADMIN && id == null) {
            return paymentRepository.findAll(pageable)
                .map(paymentMapper::toDto);
        }
        if (user.getRole() == Role.ROLE_ADMIN && id != null) {
            return paymentRepository.findByBookingUserId(id, pageable)
                .map(paymentMapper::toDto);
        }
        if (!id.equals(user.getId())) {
            throw new PaymentAccessDeniedException(
                "You only have access to your own payments!"
            );
        }
        return paymentRepository.findByBookingUserId(user.getId(), pageable)
            .map(paymentMapper::toDto);
    }

    @Override
    public CreatePaymentResponseDto createPayment(
            CreatePaymentRequestDto requestDto,
            User user,
            String baseUrl
    ) {
        Stripe.apiKey = stripeApiKey;
        Booking booking = bookingRepository.findWithAccommodationByIdAndUserId(
                requestDto.bookingId(), user.getId()
        ).orElseThrow(() -> new PaymentAccessDeniedException(
                "Not authorized to pay for a booking id: " + requestDto.bookingId())
        );
        Payment payment = new Payment();
        payment.setBooking(booking);
        BigDecimal total = countAmountToPay(booking);
        payment.setAmountToPay(total);
        long amountInCents = total.multiply(BigDecimal.valueOf(100)).longValueExact();
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(baseUrl + SUCCESS_URL)
                .setCancelUrl(baseUrl + CANCEL_URL)
                .addLineItem(
                    SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                             .setCurrency("usd")
                                .setUnitAmount(amountInCents)
                                .setProductData(
                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName(TOTAL_STRING)
                                        .build()
                                )
                                .build()
                        )
                        .build()
                )
                .build();
        Session session;
        try {
            session = Session.create(params);
        } catch (StripeException e) {
            throw new StripeSessionException("Error occurred while creating Stripe session.");
        }
        payment.setSessionId(session.getId());
        payment.setUrl(session.getUrl());
        paymentRepository.save(payment);
        return new CreatePaymentResponseDto(session.getUrl(), session.getId());
    }

    private BigDecimal countAmountToPay(Booking booking) {
        return BigDecimal.valueOf(ChronoUnit.DAYS.between(
                booking.getCheckInDate(),
                booking.getCheckOutDate()
            ))
            .multiply(booking.getAccommodation().getDailyRate());
    }
}
