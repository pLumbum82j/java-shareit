package ru.practicum.shareit.item.models.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.models.dto.BookingShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Модель объекта Item Data Transfer Object
 */
@Getter
@Setter
@Builder
public class ItemDto {
    private final Long id;
    @NotBlank(message = "Поле name не может быть пустым")
    private final String name;
    @NotBlank(message = "Поле description не может быть пустым")
    private final String description;
    @NotNull(message = "Поле available не может быть пустым")
    private final Boolean available;
    private Long requestId;
    private BookingShortDto lastBooking;
    private BookingShortDto nextBooking;
    private final List<CommentDto> comments;
}
