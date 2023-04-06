package ru.practicum.shareit.user.services;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.shareit.exceptions.ObjectAlreadyExistsException;
import ru.practicum.shareit.exceptions.ObjectUnknownException;
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
        containUserId(userId);
        log.debug("Получен запрос на поиск пользователя по ID {}", userId);
        return UserMapper.toUserDto(userStorage.get(userId));
    }

    @Override
    public UserDto create(UserDto userDto) {
        User userTemp = UserMapper.toUser(userDto);
//        if (userStorage.isContainUserId(userTemp.getId())) {
//            throw new ObjectUnknownException("Пользователь с ID " + userTemp.getId() + " уже существует");
//        }
        if (userStorage.get().stream().anyMatch(user -> user.getEmail().equals(userTemp.getEmail()))) {
            throw new ObjectAlreadyExistsException("Пользователь с ID " + userTemp.getEmail() + " уже существует");
        }

        User userResult = userStorage.create(userTemp);
        log.debug("Пользователь с именем {} успешно создан", userTemp.getName());
        return UserMapper.toUserDto(userResult);
    }

    public UserDto update(Long userId, UserDto userDto) {
        containUserId(userId);
        User userTemp = UserMapper.toUser(userDto);
        log.debug("Пользователь с ID {} успешно изменён", userId);
        return UserMapper.toUserDto(userStorage.update(userId, userTemp));
    }

    @Override
    public void delete(Long userId) {
        containUserId(userId);
        log.debug("Пользователь с ID {} успешно удалён", userId);
        userStorage.delete(userId);
    }


    /**
     * Метод проверки присутсивя пользователя на сервере
     */
    private void containUserId(long userId) {
        if (!userStorage.isContainUserId(userId)) {
            throw new ObjectUnknownException("Пользователь с ID " + userId + " не существует");
        }
    }
}
