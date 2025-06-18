package com.olieniev.bookingapp.service;

import static com.olieniev.bookingapp.util.UserUtil.getCreateUserRequestDto;
import static com.olieniev.bookingapp.util.UserUtil.getDtoAfterUpdate;
import static com.olieniev.bookingapp.util.UserUtil.getUpdateRequestDto;
import static com.olieniev.bookingapp.util.UserUtil.getUpdateRoleResponseDto;
import static com.olieniev.bookingapp.util.UserUtil.getUser;
import static com.olieniev.bookingapp.util.UserUtil.getUserDto;
import static com.olieniev.bookingapp.util.UserUtil.getUserWithIdAndRole;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.olieniev.bookingapp.dto.user.CreateUserRequestDto;
import com.olieniev.bookingapp.dto.user.UpdateRoleRequestDto;
import com.olieniev.bookingapp.dto.user.UpdateRoleResponseDto;
import com.olieniev.bookingapp.dto.user.UpdateUserRequestDto;
import com.olieniev.bookingapp.dto.user.UserDto;
import com.olieniev.bookingapp.mapper.UserMapper;
import com.olieniev.bookingapp.model.Role;
import com.olieniev.bookingapp.model.User;
import com.olieniev.bookingapp.repository.UserRepository;
import com.olieniev.bookingapp.service.user.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("""
        Saving new user returns user Dto
            """)
    public void saveUser_validData_success() {
        CreateUserRequestDto requestDto = getCreateUserRequestDto();
        User user = getUser();
        UserDto userDto = getUserDto();

        when(userRepository.existsByEmail(requestDto.email())).thenReturn(false);
        when(userMapper.toModel(requestDto)).thenReturn(user);
        when(passwordEncoder.encode(requestDto.password())).thenReturn("12345678");
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        UserDto actual = userService.register(requestDto);

        assertEquals(userDto, actual);
        verify(userRepository).existsByEmail(requestDto.email());
        verify(userMapper).toModel(requestDto);
        verify(passwordEncoder).encode(requestDto.password());
        verify(userRepository).save(user);
        verify(userMapper).toUserDto(user);
    }

    @Test
    @DisplayName("""
        Updating user role returns update role response Dto
            """)
    public void updateUserRole_validUserAndRole_success() {
        Long id = 1L;
        UpdateRoleRequestDto requestDto = new UpdateRoleRequestDto(Role.ROLE_MANAGER);
        User user = getUserWithIdAndRole();
        UpdateRoleResponseDto expected = getUpdateRoleResponseDto();

        when(userRepository.getReferenceById(id)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUpdateRoleResponseDto(user)).thenReturn(expected);

        UpdateRoleResponseDto actual = userService.updateRole(user, id, requestDto);

        assertEquals(expected, actual);
        verify(userRepository).getReferenceById(id);
        verify(userRepository).save(user);
        verify(userMapper).toUpdateRoleResponseDto(user);
    }

    @Test
    @DisplayName("""
        Updating user returns user Dto
            """)
    public void updateUser_validUserData_success() {
        User user = getUserWithIdAndRole();
        user.setEmail("even newer");
        UserDto expected = getDtoAfterUpdate();
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(expected);

        UpdateUserRequestDto requestDto = getUpdateRequestDto();
        UserDto actual = userService.updateUser(user, requestDto);

        assertEquals(expected, actual);
        verify(userRepository).save(user);
        verify(userMapper).toUserDto(user);
    }

    @Test
    @DisplayName("""
        Get user info returns user dto
            """)
    public void getUserInfo_userExists_success() {
        User user = getUserWithIdAndRole();
        UserDto expected = getUserDto();

        when(userMapper.toUserDto(user)).thenReturn(expected);

        UserDto actual = userService.getUserInfo(user);

        assertEquals(expected, actual);
        verify(userMapper).toUserDto(user);
    }
}
