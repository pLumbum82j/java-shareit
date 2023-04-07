package ru.practicum.shareit.item.services;

import ru.practicum.shareit.item.models.dto.ItemDto;

import java.util.List;

public interface ItemService {
    List<ItemDto> get();
}
