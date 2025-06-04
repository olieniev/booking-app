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
import com.olieniev.bookingapp.service.notification.NotificationService;
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
    private final NotificationService notificationService;

    @Override
    public AccommodationDto save(User user, CreateAccommodationRequestDto requestDto) {
        Accommodation accommodation = accommodationMapper.toModel(requestDto);
        accommodation.setOwner(user);
        AccommodationDto dto = accommodationMapper.toDto(
                accommodationRepository.save(accommodation)
        );
        notificationService.notify(createNotification(dto));
        return dto;
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
        AccommodationDto dto = accommodationMapper.toDto(
                accommodationRepository.save(accommodation)
        );
        notificationService.notify(updateNotification(id, user, dto));
        return dto;
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

    private String createNotification(AccommodationDto dto) {
        return """
            New accommodation has been added! 📩
            Please, see accommodation details below:
            %s
            """.formatted(dto);
    }

    private String updateNotification(Long id, User user, AccommodationDto dto) {
        return """
            Existing booking with id %d has been updated by a user with id %d and a role - %s!✏️
            New details:
            %s
            """.formatted(id, user.getId(), user.getRole(), dto);
    }
}
