package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.models.Item;
import ru.practicum.shareit.request.models.ItemRequest;

import java.util.List;

/**
 * Интерфейс CommentRepository для обработки запросов к БД
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByOwnerIdOrderByIdAsc(Long userId);

    List<Item> findAllByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(String name, String description);

    List<Item> findAllByRequestIn(List<ItemRequest> itemRequests);
}

