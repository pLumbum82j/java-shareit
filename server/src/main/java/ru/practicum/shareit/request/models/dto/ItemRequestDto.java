package ru.practicum.shareit.request.models.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.models.dto.ItemDto;
import ru.practicum.shareit.user.models.User;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Модель объекта ItemRequest Data Transfer Object
 */
@Getter
@Setter
@Builder
public class ItemRequestDto {
    private final Long id;
    @NotBlank(message = "Поле name не может быть пустым")
    private final String description;
    private final User requestor;
    private final LocalDateTime created;
    private List<ItemDto> items;
}
