package com.olieniev.bookingapp.service.accommodation;

import com.olieniev.bookingapp.dto.accommodation.AccommodationDto;
import com.olieniev.bookingapp.dto.accommodation.AccommodationRequestDto;
import com.olieniev.bookingapp.exception.EntityNotFoundException;
import com.olieniev.bookingapp.mapper.AccommodationMapper;
import com.olieniev.bookingapp.model.Accommodation;
import com.olieniev.bookingapp.repository.AccommodationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccommodationServiceImpl implements AccommodationService {
    private final AccommodationRepository accommodationRepository;
    private final AccommodationMapper accommodationMapper;

    @Override
    public AccommodationDto save(AccommodationRequestDto requestDto) {
        return accommodationMapper.toDto(
            accommodationRepository.save(
                accommodationMapper.toModel(requestDto)
            )
        );
    }

    @Override
    public Page<AccommodationDto> findAll(Pageable pageable) {
        return accommodationRepository.findAll(pageable)
            .map(accommodationMapper::toDto);
    }

    @Override
    public AccommodationDto getById(Long id) {
        return accommodationMapper.toDto(accommodationRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(
                "Can't find an accommodation by id: " + id)
            ));
    }

    @Override
    public AccommodationDto update(Long id, AccommodationRequestDto requestDto) {
        Accommodation accommodation = accommodationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                "Can't find an accommodation by id: " + id));
        accommodationMapper.updateAccommodationFromDto(requestDto, accommodation);
        return accommodationMapper.toDto(accommodationRepository.save(accommodation));
    }

    @Override
    public void delete(Long id) {
        accommodationRepository.deleteById(id);
    }
}
