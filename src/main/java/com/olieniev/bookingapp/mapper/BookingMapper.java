package com.olieniev.bookingapp.mapper;

import com.olieniev.bookingapp.config.MapperConfig;
import com.olieniev.bookingapp.dto.booking.BookingDto;
import com.olieniev.bookingapp.dto.booking.CreateBookingRequestDto;
import com.olieniev.bookingapp.dto.booking.DetailedBookingDto;
import com.olieniev.bookingapp.model.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        config = MapperConfig.class,
        uses = {AccommodationMapper.class, UserMapper.class}
)
public interface BookingMapper {
    Booking toModel(CreateBookingRequestDto requestDto);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "accommodation.id", target = "accommodationId")
    BookingDto toDto(Booking booking);

    DetailedBookingDto toDetailedDto(Booking booking);
}
