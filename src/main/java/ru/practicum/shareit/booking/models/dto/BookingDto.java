package ru.practicum.shareit.booking.models.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.models.Item;
import ru.practicum.shareit.user.models.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@Builder
public class BookingDto {
    private final long id; // — уникальный идентификатор бронирования;
    private final LocalDateTime start; // — дата и время начала бронирования;
    private final LocalDateTime end; // — дата и время конца бронирования;
    private final Item item; // — вещь, которую пользователь бронирует;
    private final User booker; // — пользователь, который осуществляет бронирование;
    private final BookingStatus status; // — статус бронирования.
}
