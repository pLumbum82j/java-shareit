package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.models.Comment;

import java.util.List;

/**
 * Интерфейс CommentRepository для обработки запросов к БД
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByItemIdOrderByCreatedDesc(Long itemId);
}
