package ru.practicum.shareit.item.models.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * Модель объекта Comment Data Transfer Object
 */
@Getter
@Setter
@Builder
public class CommentDto {

    private final Long id;
    @NotBlank(message = "Поле name не может быть пустым")
    private final String text;
    private final String authorName;
    private final LocalDateTime created;

}
