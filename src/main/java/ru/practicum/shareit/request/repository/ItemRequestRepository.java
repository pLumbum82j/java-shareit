package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.ObjectUnknownException;
import ru.practicum.shareit.request.models.ItemRequest;

import java.util.List;

/**
 * Интерфейс ItemRequestRepository для обработки запросов к БД
 */
@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findAllByRequestorIdOrderByCreatedDesc(Long requestorId);

    List<ItemRequest> findAllByRequestorIdNot(Long userId, Pageable pageable);

}
