package ru.practicum.shareit.item.models.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.models.Comment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * Модель объекта Item Data Transfer Object
 */
@Data
@Builder
public class ItemDto {
    private final Long id;
    @NotBlank(message = "Поле name не может быть пустым")
    private final String name;
    @NotBlank(message = "Поле description не может быть пустым")
    private final String description;
    @NotNull(message = "Поле available не может быть пустым")
    private final Boolean available;
    private final Long request;
    private final List<CommentDto> comments;
}
