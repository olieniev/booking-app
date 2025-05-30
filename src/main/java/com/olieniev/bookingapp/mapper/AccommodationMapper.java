package com.olieniev.bookingapp.mapper;

import com.olieniev.bookingapp.config.MapperConfig;
import com.olieniev.bookingapp.dto.accommodation.AccommodationDto;
import com.olieniev.bookingapp.dto.accommodation.CreateAccommodationRequestDto;
import com.olieniev.bookingapp.dto.accommodation.UpdateAccommodationRequestDto;
import com.olieniev.bookingapp.model.Accommodation;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", config = MapperConfig.class)
public interface AccommodationMapper {
    Accommodation toModel(CreateAccommodationRequestDto requestDto);

    AccommodationDto toDto(Accommodation accommodation);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAccommodationFromDto(
            UpdateAccommodationRequestDto requestDto, @MappingTarget Accommodation accommodation
        );
}
