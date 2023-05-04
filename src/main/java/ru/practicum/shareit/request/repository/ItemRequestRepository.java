package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.models.ItemRequest;

/**
 * Интерфейс ItemRequestRepository для обработки запросов к БД
 */
@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
}
