package ru.practicum.shareit.item.storages;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.models.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Клас ItemStorageInMemory для хранения Item в оперативной памяти сервера
 */
@Repository
public class ItemStorageInMemory implements ItemStorage {
    private final Map<Long, Item> itemList = new HashMap<>();
    private Long id = 1L;

    /**
     * Генератор ID
     *
     * @return ID
     */
    private Long generatorId() {
        return id++;
    }

    @Override
    public List<Item> get() {
        return new ArrayList<>(itemList.values());
    }

    @Override
    public Item get(Long itemId) {
        return itemList.get(itemId);
    }

    @Override
    public List<Item> search(String text) {
        return new ArrayList<>(itemList.values()).stream().filter(Item::getAvailable)
                .filter(item ->
                        item.getName().toLowerCase().contains(text) || item.getDescription().toLowerCase().contains(text))
                .collect(Collectors.toList());
    }

    @Override
    public Item create(Item item) {
        if (item.getId() == null) {
            item.setId(generatorId());
        }
        itemList.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Long itemId, Item item) {
        itemList.replace(itemId, item);
        return item;
    }

    @Override
    public void delete(Long itemId) {
        itemList.remove(itemId);
    }

    @Override
    public boolean isContainItemId(Long itemId) {
        return itemList.containsKey(itemId);
    }
}
