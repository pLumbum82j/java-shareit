package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.user.models.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
public class ItemRequestDto {
    int id;
    String description;
    User requestor;
    LocalDateTime created;
}
