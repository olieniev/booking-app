package com.olieniev.bookingapp.controller;

import com.olieniev.bookingapp.dto.accommodation.AccommodationDto;
import com.olieniev.bookingapp.dto.accommodation.CreateAccommodationRequestDto;
import com.olieniev.bookingapp.dto.accommodation.UpdateAccommodationRequestDto;
import com.olieniev.bookingapp.model.User;
import com.olieniev.bookingapp.service.accommodation.AccommodationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accommodations")
@RequiredArgsConstructor
@Tag(name = "Controller for Accommodation class",
        description = "All methods available of Accommodation controller")
public class AccommodationController {
    private final AccommodationService accommodationService;

    @PostMapping
    @Operation(summary = "Create an accommodation method",
            description = "Creates an accommodation with given parameters")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @ResponseStatus(HttpStatus.CREATED)
    public AccommodationDto createAccommodation(Authentication authentication,
            @RequestBody @Valid CreateAccommodationRequestDto requestDto) {
        return accommodationService.save((User) authentication.getPrincipal(), requestDto);
    }

    @GetMapping
    @Operation(summary = "Get accommodations method",
            description = "Returns all accommodations available in DB")
    public Page<AccommodationDto> getAll(Pageable pageable) {
        return accommodationService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get accommodation by id method",
            description = "Returns accommodation by provided id")
    public AccommodationDto getById(@PathVariable Long id) {
        return accommodationService.getById(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update accommodation method",
            description = "Updates accommodation by given id and parameters")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public AccommodationDto updateAccommodation(Authentication authentication,
            @PathVariable Long id, @RequestBody @Valid UpdateAccommodationRequestDto requestDto
    ) {
        return accommodationService.update((User) authentication.getPrincipal(), id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete accommodation method",
            description = "Deletes accommodation by given id")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public void delete(Authentication authentication, @PathVariable Long id) {
        accommodationService.delete((User) authentication.getPrincipal(), id);
    }

}
