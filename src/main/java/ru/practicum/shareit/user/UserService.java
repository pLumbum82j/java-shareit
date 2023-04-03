package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {
    List<User> get();
    User create(User user);
}
