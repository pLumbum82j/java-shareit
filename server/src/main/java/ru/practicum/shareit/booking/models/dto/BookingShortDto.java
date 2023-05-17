package ru.practicum.shareit.booking.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Модель объекта Booking Short Data Transfer Object
 */

@Getter
@Setter
@AllArgsConstructor
public class BookingShortDto {
    private final Long id;
    private final Long bookerId;
}