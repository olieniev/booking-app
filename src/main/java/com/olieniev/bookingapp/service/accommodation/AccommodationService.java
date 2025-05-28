package com.olieniev.bookingapp.service.accommodation;

import com.olieniev.bookingapp.dto.accommodation.AccommodationDto;
import com.olieniev.bookingapp.dto.accommodation.AccommodationRequestDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccommodationService {
    AccommodationDto save(@Valid AccommodationRequestDto requestDto);

    Page<AccommodationDto> findAll(Pageable pageable);

    AccommodationDto getById(Long id);

    AccommodationDto update(Long id, @Valid AccommodationRequestDto requestDto);

    void delete(Long id);
}
