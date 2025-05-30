package com.olieniev.bookingapp.service.accommodation;

import com.olieniev.bookingapp.dto.accommodation.AccommodationDto;
import com.olieniev.bookingapp.dto.accommodation.CreateAccommodationRequestDto;
import com.olieniev.bookingapp.dto.accommodation.UpdateAccommodationRequestDto;
import com.olieniev.bookingapp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccommodationService {
    AccommodationDto save(User user, CreateAccommodationRequestDto requestDto);

    Page<AccommodationDto> findAll(Pageable pageable);

    AccommodationDto getById(Long id);

    AccommodationDto update(User user, Long id, UpdateAccommodationRequestDto requestDto);

    void delete(User user, Long id);
}
