package ru.practicum.shareit.user;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class UserServiceInMemory implements UserService{

    private final UserStorage userStorage;

    public UserServiceInMemory(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public List<User> get() {
        log.debug("Получен запрос на список пользователей");
        return userStorage.get();
    }

    @Override
    public User create(User user) {
        userStorage.create(user);
        log.debug("Пользователь с именем {} успешно создан", user.getName());
        return user;
    }
}
