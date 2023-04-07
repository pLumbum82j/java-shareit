package ru.practicum.shareit.item.storages;

import ru.practicum.shareit.item.models.Item;
import ru.practicum.shareit.item.models.dto.ItemDto;

import java.util.List;

public interface ItemStorage {
    List<Item> get();

}
