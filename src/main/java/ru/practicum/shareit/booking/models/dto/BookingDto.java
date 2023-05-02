package ru.practicum.shareit.booking.models.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.models.Item;
import ru.practicum.shareit.user.models.User;

import java.time.LocalDateTime;

/**
 * Модель объекта Booking Data Transfer Object
 */
@Data
@Builder
public class BookingDto {
    private final long id;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final Item item;
    private final User booker;
    private final BookingStatus status;
}
