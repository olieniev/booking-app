package com.olieniev.bookingapp.controller;

import com.olieniev.bookingapp.dto.user.UpdateRoleRequestDto;
import com.olieniev.bookingapp.dto.user.UpdateRoleResponseDto;
import com.olieniev.bookingapp.dto.user.UpdateUserRequestDto;
import com.olieniev.bookingapp.dto.user.UserDto;
import com.olieniev.bookingapp.model.User;
import com.olieniev.bookingapp.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Controller for User class",
        description = "All methods available of User controller")
public class UserController {
    private final UserService userService;

    @PutMapping("/{id}/role")
    @Operation(summary = "Update user role method",
            description = "Updates user role by given id and provided role")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','USER')")
    public UpdateRoleResponseDto updateUserRole(
            Authentication authentication,
            @PathVariable Long id,
            @RequestBody UpdateRoleRequestDto requestDto
    ) {
        return userService.updateRole((User) authentication.getPrincipal(), id, requestDto);
    }

    @GetMapping("/me")
    @Operation(summary = "Get user info method",
            description = "Returns user info of authenticated user")
    public UserDto getUserInfo(Authentication authentication) {
        return userService.getUserInfo((User) authentication.getPrincipal());
    }

    @PatchMapping("/me")
    @Operation(summary = "Update user info method",
            description = "Returns user info of authenticated user")
    @PreAuthorize("hasAnyRole('MANAGER','USER')")
    public UserDto updateUser(Authentication authentication,
                              @RequestBody UpdateUserRequestDto requestDto) {
        return userService.updateUser((User) authentication.getPrincipal(), requestDto);
    }
}
