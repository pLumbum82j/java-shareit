package ru.practicum.shareit.request.mappers;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.request.models.ItemRequest;
import ru.practicum.shareit.request.models.dto.ItemRequestDto;

import java.time.LocalDateTime;

/**
 * Класс ItemRequestMapper для преобразования ItemRequest в ItemRequestDto и обратно
 */
@UtilityClass
public class ItemRequestMapper {

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated() == null ? LocalDateTime.now() : itemRequest.getCreated())
                .build();
    }

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto){
        return ItemRequest.builder()
                .description(itemRequestDto.getDescription())
                .created(itemRequestDto.getCreated() == null ? LocalDateTime.now() : itemRequestDto.getCreated())
                .build();
    }

}
