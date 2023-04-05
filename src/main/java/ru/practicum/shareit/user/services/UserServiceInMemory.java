package ru.practicum.shareit.user.services;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.shareit.user.mappers.UserMapper;
import ru.practicum.shareit.user.models.dto.UserDto;
import ru.practicum.shareit.user.storages.UserStorage;
import ru.practicum.shareit.user.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class UserServiceInMemory implements UserService {

    private final UserStorage userStorage;

    public UserServiceInMemory(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public List<UserDto> get() {
        log.debug("Получен запрос на список пользователей");
        return userStorage.get().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    public UserDto get(@PathVariable Long userId) {
        log.debug("Получен запрос на поиск пользователя по ID");
        return UserMapper.toUserDto(userStorage.get(userId));
    }

    @Override
    public UserDto create(UserDto userDto) {
        User userTemp = UserMapper.toUser(userDto);
        User userResult = userStorage.create(userTemp);
        log.debug("Пользователь с именем {} успешно создан", userTemp.getName());
        return UserMapper.toUserDto(userResult);
    }
}
