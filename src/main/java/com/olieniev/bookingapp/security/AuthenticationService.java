package com.olieniev.bookingapp.security;

import com.olieniev.bookingapp.dto.user.UserLoginRequestDto;
import com.olieniev.bookingapp.dto.user.UserLoginResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UserLoginResponseDto authenticate(UserLoginRequestDto userLoginRequestDto) {
        final Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                userLoginRequestDto.email(), userLoginRequestDto.password()
            ));
        return new UserLoginResponseDto(jwtUtil.generateToken(authentication.getName()));
    }
}
