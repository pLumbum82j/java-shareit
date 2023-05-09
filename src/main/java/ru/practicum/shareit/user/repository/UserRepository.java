package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.ObjectUnknownException;
import ru.practicum.shareit.user.models.User;

/**
 * Интерфейс UserRepository для обработки запросов к БД
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
//    default User get(long id) {
//        return findById(id).orElseThrow(() -> new ObjectUnknownException("Пользователь с ID: " + id + " не существует"));
//    }
}
