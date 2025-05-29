package com.olieniev.bookingapp.controller;

import com.olieniev.bookingapp.dto.user.UserLoginRequestDto;
import com.olieniev.bookingapp.dto.user.UserLoginResponseDto;
import com.olieniev.bookingapp.security.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication controller",
        description = "All methods of authentication controller")
public class AuthController {
    private final AuthenticationService authenticationService;

    @Operation(summary = "Authenticate(login) method",
            description = "Returns JWT token")
    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }

    @Operation(summary = "Registration method",
            description = "Returns registered user")
    @PostMapping("/register")
    public UserLoginResponseDto register(/*TODO*/) {
        return null;
    }
}
