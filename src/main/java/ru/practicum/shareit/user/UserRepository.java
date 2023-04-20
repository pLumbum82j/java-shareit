package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.exceptions.ObjectUnknownException;
import ru.practicum.shareit.user.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    default User get(long id) {
        return findById(id).orElseThrow(() -> new ObjectUnknownException("Пользователь с ID: " + id + " не существует"));
    }
}
