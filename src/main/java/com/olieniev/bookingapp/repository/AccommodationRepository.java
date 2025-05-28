package com.olieniev.bookingapp.repository;

import com.olieniev.bookingapp.model.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
}
