package com.olieniev.bookingapp.service.accommodation;

import com.olieniev.bookingapp.dto.accommodation.AccommodationDto;
import com.olieniev.bookingapp.dto.accommodation.CreateAccommodationRequestDto;
import com.olieniev.bookingapp.dto.accommodation.UpdateAccommodationRequestDto;
import com.olieniev.bookingapp.exception.EntityNotFoundException;
import com.olieniev.bookingapp.exception.OwnerNotAuthorisedException;
import com.olieniev.bookingapp.mapper.AccommodationMapper;
import com.olieniev.bookingapp.model.Accommodation;
import com.olieniev.bookingapp.model.Role;
import com.olieniev.bookingapp.model.User;
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
    public AccommodationDto save(User user, CreateAccommodationRequestDto requestDto) {
        Accommodation accommodation = accommodationMapper.toModel(requestDto);
        accommodation.setOwner(user);
        return accommodationMapper.toDto(accommodationRepository.save(accommodation));
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
    public AccommodationDto update(User user, Long id, UpdateAccommodationRequestDto requestDto) {
        Accommodation accommodation = accommodationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                "Can't find an accommodation by id: " + id));
        if (!user.getRole().equals(Role.ROLE_ADMIN) && !isOwner(user, accommodation)) {
            throw new OwnerNotAuthorisedException("Not authorised to update the accommodation!");
        }
        accommodationMapper.updateAccommodationFromDto(requestDto, accommodation);
        return accommodationMapper.toDto(accommodationRepository.save(accommodation));
    }

    @Override
    public void delete(User user, Long id) {
        Accommodation accommodation = accommodationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                "Can't find an accommodation by id: " + id));
        if (!user.getRole().equals(Role.ROLE_ADMIN) && !isOwner(user, accommodation)) {
            throw new OwnerNotAuthorisedException("Not authorised to delete the accommodation!");
        }
        accommodationRepository.deleteById(id);
    }

    private boolean isOwner(User user, Accommodation accommodation) {
        return accommodation.getOwner().equals(user);
    }
}
