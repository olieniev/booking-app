package com.olieniev.bookingapp.util;

import com.olieniev.bookingapp.dto.user.CreateUserRequestDto;
import com.olieniev.bookingapp.dto.user.UpdateRoleResponseDto;
import com.olieniev.bookingapp.dto.user.UpdateUserRequestDto;
import com.olieniev.bookingapp.dto.user.UserDto;
import com.olieniev.bookingapp.model.Role;
import com.olieniev.bookingapp.model.User;

public class UserUtil {
    public static CreateUserRequestDto getCreateUserRequestDto() {
        return new CreateUserRequestDto(
            "create@email.com",
            "new",
            "very new",
            "12345678"
        );
    }

    public static User getUser() {
        User user = new User();
        user.setEmail("create@email.com");
        user.setFirstName("new");
        user.setLastName("very new");
        user.setPassword("12345678");
        return user;
    }

    public static User getUserWithIdAndRole() {
        User user = new User();
        user.setId(1L);
        user.setEmail("create@email.com");
        user.setFirstName("new");
        user.setLastName("very new");
        user.setPassword("12345678");
        user.setRole(Role.ROLE_USER);
        return user;
    }

    public static UserDto getUserDto() {
        return new UserDto(
            1L,
            "create@email.com",
            "new",
            "very new",
            Role.ROLE_USER
        );
    }

    public static UpdateRoleResponseDto getUpdateRoleResponseDto() {
        return new UpdateRoleResponseDto(
            1L,
            "create@email.com",
            Role.ROLE_MANAGER
            );
    }

    public static UpdateUserRequestDto getUpdateRequestDto() {
        return new UpdateUserRequestDto(
            null,
            "even newer",
            null
        );
    }

    public static UserDto getDtoAfterUpdate() {
        return new UserDto(
            1L,
            "create@email.com",
            "even newer",
            "very new",
            Role.ROLE_USER
        );
    }
}
