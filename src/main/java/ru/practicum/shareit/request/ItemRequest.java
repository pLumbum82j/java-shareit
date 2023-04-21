package ru.practicum.shareit.request;

import lombok.Data;
import lombok.NonNull;
import ru.practicum.shareit.user.models.User;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * TODO Sprint add-item-requests.
 */
@Data
public class ItemRequest {
    private int id; // — уникальный идентификатор запроса;
    @NonNull
    private String description; // — текст запроса, содержащий описание требуемой вещи;
    private User requestor; // — пользователь, создавший запрос;
    private LocalDateTime created; // — дата и время создания запроса.
}
