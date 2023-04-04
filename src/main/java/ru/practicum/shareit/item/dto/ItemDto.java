package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private String owner;
    private ItemRequest request; //Пока не понятен тип поля.

    public ItemDto(String name, String description, Boolean available, Long aLong) {
    }
}
