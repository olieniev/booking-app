package com.olieniev.bookingapp.controller;

import com.olieniev.bookingapp.dto.payment.CreatePaymentRequestDto;
import com.olieniev.bookingapp.dto.payment.CreatePaymentResponseDto;
import com.olieniev.bookingapp.dto.payment.PaymentDto;
import com.olieniev.bookingapp.model.User;
import com.olieniev.bookingapp.service.payment.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
@Tag(name = "Controller for Payment class",
        description = "All methods of Payment controller")
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping()
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','USER')")
    @Operation(summary = "Get payments method",
            description = "Returns payments user is authorized to retrieve")
    public Page<PaymentDto> getPayments(
            @RequestParam(required = false) Long id,
            Authentication authentication,
            Pageable pageable
    ) {
        return paymentService.getPaymentsById(id, (User) authentication.getPrincipal(), pageable);
    }

    @PostMapping()
    @PreAuthorize("hasAnyRole('MANAGER','USER')")
    @Operation(summary = "Initiate payment session",
            description = "Initiates payment session for desired booking")
    @ResponseStatus(HttpStatus.CREATED)
    public CreatePaymentResponseDto makePayment(
            @RequestBody @Valid CreatePaymentRequestDto requestDto,
            Authentication authentication,
            HttpServletRequest request
    ) {
        String baseUrl = UriComponentsBuilder
                .fromUriString(request.getRequestURL().toString())
                .replacePath(null)
                .build()
                .toUriString();
        return paymentService.createPayment(
            requestDto,
            (User) authentication.getPrincipal(),
            baseUrl
        );
    }

    @GetMapping("/success")
    @PreAuthorize("hasAnyRole('MANAGER','USER')")
    @Operation(summary = "Successful payment method",
            description = "Handles successful payment processing through Stripe redirection")
    public ResponseEntity<Map<String, String>> paymentSuccess(
            @RequestParam("session_id") String sessionId
    ) {
        return paymentService.handleSuccess(sessionId);
    }

    @GetMapping("/cancel")
    @PreAuthorize("hasAnyRole('MANAGER','USER')")
    @Operation(summary = "Unsuccessful payment method",
            description = "Handles NOT successful payment")
    public ResponseEntity<Map<String, String>> paymentCancel() {
        return ResponseEntity.ok(Map.of(
            "status", "canceled",
            "message", "Payment paused OR canceled. "
                + "Please, note: payment can be accomplished within 24 hours only"
        ));
    }
}
