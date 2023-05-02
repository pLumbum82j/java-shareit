package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.models.User;

import java.time.LocalDateTime;

/**
 * Модель объекта ItemRequest
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest {
    private int id;
    private String description;
    private User requestor;
    private LocalDateTime created;
}
