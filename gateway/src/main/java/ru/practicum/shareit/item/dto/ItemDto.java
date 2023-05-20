package ru.practicum.shareit.item.dto;

import lombok.Getter;
import ru.practicum.shareit.booking.dto.BookingShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Модель объекта Item Data Transfer Object
 */
@Getter
public class ItemDto {
    private Long id;
    @NotBlank(message = "Поле name не может быть пустым")
    private String name;
    @NotBlank(message = "Поле description не может быть пустым")
    private String description;
    @NotNull(message = "Поле available не может быть пустым")
    private Boolean available;
    private Long requestId;
    private BookingShortDto lastBooking;
    private BookingShortDto nextBooking;
    private List<CommentDto> comments;
}