package ru.practicum.shareit.item.services;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.models.dto.ItemDto;
import ru.practicum.shareit.item.storages.ItemStorage;
import ru.practicum.shareit.user.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class ItemServiceInMemory implements ItemService{
    private final ItemStorage itemStorage;

    public ItemServiceInMemory(ItemStorage itemStorage) {
        this.itemStorage = itemStorage;
    }

    @Override
    public List<ItemDto> get() {
        return itemStorage.get().stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }
}
