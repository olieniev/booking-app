package com.olieniev.bookingapp.mapper;

import com.olieniev.bookingapp.config.MapperConfig;
import com.olieniev.bookingapp.dto.accommodation.AccommodationDto;
import com.olieniev.bookingapp.dto.accommodation.AccommodationRequestDto;
import com.olieniev.bookingapp.model.Accommodation;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", config = MapperConfig.class)
public interface AccommodationMapper {
    Accommodation toModel(AccommodationRequestDto requestDto);

    AccommodationDto toDto(Accommodation accommodation);

    void updateAccommodationFromDto(
            AccommodationRequestDto requestDto, @MappingTarget Accommodation accommodation
        );
}
