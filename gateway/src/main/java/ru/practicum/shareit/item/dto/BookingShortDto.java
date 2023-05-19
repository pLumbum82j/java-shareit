package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class BookingShortDto {
    /**
     * Модель объекта Booking Short Data Transfer Object
     */

    @Getter
    @Setter
    @AllArgsConstructor
    public class BookingShortGatweayDto {
        private final Long id;
        private final Long bookerId;
    }
}
