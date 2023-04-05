package ru.practicum.shareit.user.storages;

import ru.practicum.shareit.user.models.User;

import java.util.List;

public interface UserStorage {
    List<User> get();
    User get(Long userId);
    User create(User user);
}
