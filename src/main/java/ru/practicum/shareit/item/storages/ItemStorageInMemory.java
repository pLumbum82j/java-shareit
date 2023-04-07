package ru.practicum.shareit.item.storages;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.models.Item;
import ru.practicum.shareit.item.models.dto.ItemDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemStorageInMemory implements ItemStorage {
    private final Map<Integer, Item> itemList = new HashMap<>();

    @Override
    public List<Item> get() {
        return new ArrayList<>(itemList.values());
    }
}
