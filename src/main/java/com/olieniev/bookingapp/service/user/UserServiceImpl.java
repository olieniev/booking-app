package com.olieniev.bookingapp.service.user;

import com.olieniev.bookingapp.dto.user.CreateUserRequestDto;
import com.olieniev.bookingapp.dto.user.UpdateRoleRequestDto;
import com.olieniev.bookingapp.dto.user.UpdateRoleResponseDto;
import com.olieniev.bookingapp.dto.user.UpdateUserRequestDto;
import com.olieniev.bookingapp.dto.user.UserDto;
import com.olieniev.bookingapp.exception.RegistrationException;
import com.olieniev.bookingapp.exception.UnauthorisedRoleChangeException;
import com.olieniev.bookingapp.mapper.UserMapper;
import com.olieniev.bookingapp.model.Role;
import com.olieniev.bookingapp.model.User;
import com.olieniev.bookingapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto register(CreateUserRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.email())) {
            throw new RegistrationException("User already exists!");
        }
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.password()));
        user.setRole(Role.ROLE_USER);
        return userMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public UpdateRoleResponseDto updateRole(User user, Long id, UpdateRoleRequestDto requestDto) {
        if (isNonAdmin(user) && requestDto.role().equals(Role.ROLE_ADMIN)) {
            throw new UnauthorisedRoleChangeException("Unauthorised role change to Admin!");
        }
        if (!isAdmin(user) && !user.getId().equals(id)) {
            throw new UnauthorisedRoleChangeException(
                "Unauthorised role change of a different user!"
            );
        }
        if (isAdmin(user) && user.getId().equals(id)) {
            throw new UnauthorisedRoleChangeException(
                "Unauthorised: admin must stay admin!"
            );
        }
        User updatedUser = userRepository.getReferenceById(id);
        updatedUser.setRole(requestDto.role());
        return userMapper.toUpdateRoleResponseDto(userRepository.save(updatedUser));
    }

    @Override
    public UserDto getUserInfo(User user) {
        return userMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(User user, UpdateUserRequestDto requestDto) {
        userMapper.updateUserFromDto(requestDto, user);
        return userMapper.toUserDto(userRepository.save(user));
    }

    private boolean isAdmin(User user) {
        return user.getRole().equals(Role.ROLE_ADMIN);
    }

    private boolean isNonAdmin(User user) {
        return user.getRole().equals(Role.ROLE_USER) || user.getRole().equals(Role.ROLE_MANAGER);
    }
}
