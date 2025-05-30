package com.olieniev.bookingapp.service.user;

import com.olieniev.bookingapp.dto.user.CreateUserRequestDto;
import com.olieniev.bookingapp.dto.user.UpdateRoleRequestDto;
import com.olieniev.bookingapp.dto.user.UpdateRoleResponseDto;
import com.olieniev.bookingapp.dto.user.UpdateUserRequestDto;
import com.olieniev.bookingapp.dto.user.UserDto;
import com.olieniev.bookingapp.model.User;
import jakarta.validation.Valid;

public interface UserService {
    UserDto register(@Valid CreateUserRequestDto requestDto);

    UpdateRoleResponseDto updateRole(User user, Long id, UpdateRoleRequestDto requestDto);

    UserDto getUserInfo(User user);

    UserDto updateUser(User user, UpdateUserRequestDto requestDto);
}
