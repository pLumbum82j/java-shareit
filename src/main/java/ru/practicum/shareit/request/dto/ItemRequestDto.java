package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.models.User;

import java.time.LocalDateTime;

/**
 * Модель объекта ItemRequest Data Transfer Object
 */
@Data
@Builder
public class ItemRequestDto {
   private final int id;
    private final  String description;
    private final  User requestor;
    private final  LocalDateTime created;
}
