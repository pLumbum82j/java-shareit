package ru.practicum.shareit.item.models.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.models.dto.BookingDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Модель объекта Item Data Transfer Object
 */
@Data
@Builder
public class ItemDateDto {
    private final Long id;
    @NotBlank(message = "Поле name не может быть пустым")
    private final String name;
    @NotBlank(message = "Поле description не может быть пустым")
    private final String description;
    @NotNull(message = "Поле available не может быть пустым")
    private final Boolean available;
    private final Long request;
    private final BookingDto lastBooking;
    private final BookingDto nextBooking;

    static class BookingDto {
        Long id;
        Long brookerId;
    }
}