package ru.practicum.shareit.request.models.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.models.User;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * Модель объекта ItemRequest Data Transfer Object
 */
@Getter
@Setter
@Builder
public class ItemRequestDto {
   private final int id;
    @NotBlank(message = "Поле name не может быть пустым")
    private final  String description;
    private final  User requestor;
    private final  LocalDateTime created;
}
