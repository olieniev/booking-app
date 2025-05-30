package com.olieniev.bookingapp.mapper;

import com.olieniev.bookingapp.config.MapperConfig;
import com.olieniev.bookingapp.dto.user.CreateUserRequestDto;
import com.olieniev.bookingapp.dto.user.UpdateRoleResponseDto;
import com.olieniev.bookingapp.dto.user.UpdateUserRequestDto;
import com.olieniev.bookingapp.dto.user.UserDto;
import com.olieniev.bookingapp.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", config = MapperConfig.class)
public interface UserMapper {
    User toModel(CreateUserRequestDto requestDto);

    UserDto toUserDto(User user);

    UpdateRoleResponseDto toUpdateRoleResponseDto(User user);

    void updateUserFromDto(UpdateUserRequestDto requestDto, @MappingTarget User user);

}
