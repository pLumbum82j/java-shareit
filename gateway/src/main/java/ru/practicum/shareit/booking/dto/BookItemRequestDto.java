package ru.practicum.shareit.booking.dto;

import lombok.Getter;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

/**
 * Модель объекта BookItemRequest Data Transfer Object
 */
@Getter
public class BookItemRequestDto {
    private Long itemId;
    @FutureOrPresent
    private LocalDateTime start;
    @Future
    private LocalDateTime end;
}
