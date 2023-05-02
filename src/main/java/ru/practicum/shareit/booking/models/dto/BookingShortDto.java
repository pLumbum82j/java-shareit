package ru.practicum.shareit.booking.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Модель объекта Booking Short Data Transfer Object
 */

@Data
@AllArgsConstructor
public class BookingShortDto {
    private final Long id;
    private final Long bookerId;
}