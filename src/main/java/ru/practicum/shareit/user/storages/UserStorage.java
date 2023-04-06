package ru.practicum.shareit.user.storages;

import ru.practicum.shareit.user.models.User;

import java.util.List;

public interface UserStorage {
    List<User> get();

    User get(Long userId);

    User create(User user);

    User update(Long userId, User user);

    void delete(Long userId);

    boolean isContainUserId(Long userId);
}
