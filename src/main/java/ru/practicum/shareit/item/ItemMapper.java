package ru.practicum.shareit.item;

import ru.practicum.shareit.item.models.dto.ItemDto;
import ru.practicum.shareit.item.models.Item;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .request(item.getRequest() != null ? item.getRequest().getId() : null)
                .build();
    }

}