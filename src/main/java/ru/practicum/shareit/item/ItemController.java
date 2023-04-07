package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.models.dto.ItemDto;
import ru.practicum.shareit.item.services.ItemService;
import ru.practicum.shareit.user.models.dto.UserDto;
import ru.practicum.shareit.user.services.UserService;

import java.util.List;

/**
 * Класс Контроллер по энпоинту Items
 */
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    /**
     * Метод (эндпоинт) получения списка вещей
     *
     * @return Список вещей
     */
    @GetMapping()
    public List<ItemDto> get() {
        return itemService.get();
    }

}
