package com.olieniev.bookingapp.mapper;

import com.olieniev.bookingapp.config.MapperConfig;
import com.olieniev.bookingapp.dto.payment.PaymentDto;
import com.olieniev.bookingapp.model.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", config = MapperConfig.class)
public interface PaymentMapper {
    PaymentDto toDto(Payment payment);
}
