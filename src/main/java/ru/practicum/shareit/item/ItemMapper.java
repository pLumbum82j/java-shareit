package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.models.Item;
import ru.practicum.shareit.item.models.dto.ItemDto;

/**
 * Класс ItemMapper для преобразования Item в ItemDto и обратно
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {
    /**
     * Статический метод преобразования Item в ItemDto
     *
     * @param item Объект Item
     * @return Объект ItemDto
     */
    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                //.request(item.getRequest() != null ? item.getRequest().getId() : null)
                .build();
    }

    /**
     * Статический метод преобразования ItemDto в Item
     *
     * @param itemDto Объект ItemDto
     * @return Объект Item
     */
    public static Item toItem(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
    }

}
