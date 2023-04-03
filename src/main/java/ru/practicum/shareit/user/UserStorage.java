package ru.practicum.shareit.user;

import java.util.List;

public interface UserStorage {
    List<User> get();
    User create(User user);
}
