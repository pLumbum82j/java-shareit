package ru.practicum.shareit.user.services;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.shareit.exceptions.ObjectAlreadyExistsException;
import ru.practicum.shareit.exceptions.ObjectUnknownException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.models.User;
import ru.practicum.shareit.user.models.dto.UserDto;
import ru.practicum.shareit.user.storages.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс UserServiceInMemory для отработки логики запросов и логирования
 */
@Log4j2
@Service
public class UserServiceInMemory implements UserService {

    private final UserStorage userStorage;


    public UserServiceInMemory(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public List<UserDto> get() {
        log.debug("Получен запрос на список всех пользователей");
        return userStorage.get().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    public UserDto get(@PathVariable Long userId) {
        containUserId(userId);
        log.debug("Получен запрос на поиск пользователя по ID: {}", userId);
        return UserMapper.toUserDto(userStorage.get(userId));
    }

    @Override
    public UserDto create(UserDto userDto) {
        User userTemp = UserMapper.toUser(userDto);
        if (containUserName(userTemp)) {
            throw new ObjectAlreadyExistsException("Пользователь с name: " + userTemp.getName() + " уже существует");
        }
        if (containUserEmail(userTemp)) {
            throw new ObjectAlreadyExistsException("Пользователь с email: " + userTemp.getEmail() + " уже существует");
        }
        User userResult = userStorage.create(userTemp);
        log.debug("Получен запрос на создание пользователя с именем: {}", userTemp.getName());
        return UserMapper.toUserDto(userResult);
    }

    public UserDto update(Long id, UserDto userDto) {
        containUserId(id);
        User userOld = userStorage.get(id);
        User userTemp = UserMapper.toUser(userDto);
        if (containUserName(userTemp) && !userTemp.getName().equals(userOld.getName())) {
            throw new ObjectAlreadyExistsException("Пользователь с name: " + userTemp.getName() + " уже существует");
        }
        if (containUserEmail(userTemp) && !userTemp.getEmail().equals(userOld.getEmail())) {
            throw new ObjectAlreadyExistsException("Пользователь с email: " + userTemp.getEmail() + " уже существует");
        }
        if (userTemp.getEmail() != null) {
            userOld.setEmail(userTemp.getEmail());
        }
        if (userTemp.getName() != null) {
            userOld.setName(userTemp.getName());
        }
        log.debug("Получен запрос на обновление пользователя с ID: {}", id);
        return UserMapper.toUserDto(userStorage.update(id, userOld));
    }

    @Override
    public void delete(Long userId) {
        containUserId(userId);
        log.debug("Получен запрос на удаления пользователя с ID {}", userId);
        userStorage.delete(userId);
    }

    /**
     * Метод проверки пользователя на сервере по имени
     *
     * @param userN Объект User
     * @return true/false нашлось ли совпадение
     */
    private boolean containUserName(User userN) {
        return userStorage.get().stream().anyMatch(user -> user.getName().equals(userN.getName()));
    }

    /**
     * Метод проверки пользователя на сервере по эл. почте
     *
     * @param userE Объект User
     * @return true/false нашлось ли совпадение
     */
    private boolean containUserEmail(User userE) {
        return userStorage.get().stream().anyMatch(user -> user.getEmail().equals(userE.getEmail()));
    }

    /**
     * Метод проверки присутствия пользователя на сервере
     */
    private void containUserId(long userId) {
        if (!userStorage.isContainUserId(userId)) {
            throw new ObjectUnknownException("Пользователь с ID " + userId + " не существует");
        }
    }
}
