package ru.practicum.shareit.item.models.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.models.Item;
import ru.practicum.shareit.user.models.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {

    private final Long id;
    @NotBlank(message = "Поле name не может быть пустым")
    private final String text;
    private final String authorName;
    private final LocalDateTime created;

}
