package com.olieniev.bookingapp.service;

import static com.olieniev.bookingapp.util.AccommodationUtil.createAccommodationDto;
import static com.olieniev.bookingapp.util.AccommodationUtil.createAccommodationRequestDto;
import static com.olieniev.bookingapp.util.AccommodationUtil.getAccommodation;
import static com.olieniev.bookingapp.util.AccommodationUtil.getListOfAccommodations;
import static com.olieniev.bookingapp.util.AccommodationUtil.getListOfDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.olieniev.bookingapp.dto.accommodation.AccommodationDto;
import com.olieniev.bookingapp.dto.accommodation.CreateAccommodationRequestDto;
import com.olieniev.bookingapp.mapper.AccommodationMapper;
import com.olieniev.bookingapp.model.Accommodation;
import com.olieniev.bookingapp.model.Role;
import com.olieniev.bookingapp.model.User;
import com.olieniev.bookingapp.repository.AccommodationRepository;
import com.olieniev.bookingapp.service.accommodation.AccommodationServiceImpl;
import com.olieniev.bookingapp.service.notification.NotificationService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class AccommodationServiceTest {
    @Mock
    private AccommodationRepository accommodationRepository;
    @Mock
    private AccommodationMapper accommodationMapper;
    @Mock
    private NotificationService notificationService;
    @InjectMocks
    private AccommodationServiceImpl accommodationService;

    @Test
    @DisplayName("""
        Saving new accommodation returns accoommodation Dto
            """)
    public void saveAccommodation_validData_success() {
        CreateAccommodationRequestDto requestDto = createAccommodationRequestDto();
        Accommodation accommodation = getAccommodation();
        AccommodationDto expected = createAccommodationDto();

        when(accommodationMapper.toModel(requestDto)).thenReturn(accommodation);
        when(accommodationRepository.save(accommodation)).thenReturn(accommodation);
        when(accommodationMapper.toDto(accommodation)).thenReturn(expected);

        AccommodationDto actual = accommodationService.save(new User(), requestDto);

        assertEquals(expected, actual);
        verify(accommodationMapper).toModel(requestDto);
        verify(accommodationRepository).save(accommodation);
        verify(accommodationMapper).toDto(accommodation);
    }

    @Test
    @DisplayName("""
        Get accommodation by id returns AccommodationDto
            """)
    public void getAccommodation_existsInDb_success() {
        Long id = 1L;
        Accommodation accommodation = getAccommodation();
        AccommodationDto expected = createAccommodationDto();

        when(accommodationRepository.findById(id)).thenReturn(Optional.of(accommodation));
        when(accommodationMapper.toDto(accommodation)).thenReturn(expected);

        AccommodationDto actual = accommodationService.getById(id);

        assertEquals(expected, actual);
        verify(accommodationRepository).findById(id);
        verify(accommodationMapper).toDto(accommodation);
    }

    @Test
    @DisplayName("""
        Get accommodation by id returns AccommodationDto
            """)
    public void deleteAccommodation_existsInDb_returnsVoid() {
        Long id = 1L;
        Accommodation accommodation = getAccommodation();
        User user = new User();
        user.setRole(Role.ROLE_ADMIN);

        when(accommodationRepository.findById(id)).thenReturn(Optional.of(accommodation));
        doNothing().when(accommodationRepository).deleteById(id);

        accommodationService.delete(user, id);

        verify(accommodationRepository).findById(id);
        verify(accommodationRepository).deleteById(id);
    }

    @Test
    @DisplayName("""
        Get all accommodations returns list of AccommodationDto
            """)
    public void getAllAccommodations_existsInDb_success() {
        List<Accommodation> listOfAccommodations = getListOfAccommodations();
        List<AccommodationDto> expected = getListOfDto();
        Pageable pageable = PageRequest.of(0, 3);
        Page<Accommodation> page = new PageImpl<>(
                listOfAccommodations, pageable, listOfAccommodations.size()
        );

        when(accommodationRepository.findAll(pageable)).thenReturn(page);
        when(accommodationMapper.toDto(any(Accommodation.class)))
                .thenReturn(expected.get(0), expected.get(1), expected.get(2));

        Page<AccommodationDto> actual = accommodationService.findAll(pageable);

        assertEquals(expected.size(), actual.getContent().size());
        verify(accommodationRepository).findAll(pageable);
        verify(accommodationMapper, times(listOfAccommodations.size()))
                .toDto(any(Accommodation.class));
    }
}
