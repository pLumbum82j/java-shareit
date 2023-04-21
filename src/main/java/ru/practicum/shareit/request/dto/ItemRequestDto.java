package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.user.models.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
public class ItemRequestDto {
    int id; // — уникальный идентификатор запроса;
    String description; // — текст запроса, содержащий описание требуемой вещи;
    User requestor; // — пользователь, создавший запрос;
    LocalDateTime created; // — дата и время создания запроса.
}
