package ru.practicum.shareit.user.services;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.shareit.exceptions.ObjectAlreadyExistsException;
import ru.practicum.shareit.exceptions.ObjectUnknownException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.models.dto.UserDto;
import ru.practicum.shareit.user.storages.UserStorage;
import ru.practicum.shareit.user.models.User;

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
        log.debug("Получен запрос на поиск пользователя по ID: {}", userId);
        return UserMapper.toUserDto(userStorage.get(userId));
    }

    @Override
    public UserDto create(UserDto userDto) {
        User userTemp = UserMapper.toUser(userDto);

        if (containUserName(userTemp)) {
            throw new ObjectAlreadyExistsException("Пользователь с name: " + userTemp.getName() + " уже существует");
        }
        //if (userStorage.get().stream().anyMatch(user -> user.getEmail().equals(userTemp.getEmail()))) {
        if (containUserEmail(userTemp)) {
            throw new ObjectAlreadyExistsException("Пользователь с email: " + userTemp.getEmail() + " уже существует");
        }

        User userResult = userStorage.create(userTemp);
        log.debug("Пользователь с именем: {} успешно создан", userTemp.getName());
        return UserMapper.toUserDto(userResult);
    }

    public UserDto update(Long userId, UserDto userDto) {
        containUserId(userId);
        User userOld = userStorage.get(userId);
        User userTemp = UserMapper.toUser(userDto);
       // if (userStorage.get().stream().anyMatch(user -> user.getName().equals(userTemp.getName())) && !userTemp.getName().equals(userOld.getName())) {
        if (containUserName(userTemp) && !userTemp.getName().equals(userOld.getName())) {
            throw new ObjectAlreadyExistsException("Пользователь с name: " + userTemp.getName() + " уже существует");
        }
       // if (userStorage.get().stream().anyMatch(user -> user.getEmail().equals(userTemp.getEmail())) && !userTemp.getEmail().equals(userOld.getEmail())) {
        if (containUserEmail(userTemp) && !userTemp.getEmail().equals(userOld.getEmail())) {
            throw new ObjectAlreadyExistsException("Пользователь с email: " + userTemp.getEmail() + " уже существует");
        }
        if (userTemp.getEmail() != null) {
            userOld.setEmail(userTemp.getEmail());
        }
        if (userTemp.getName() != null) {
            userOld.setName(userTemp.getName());
        }
        log.debug("Пользователь с ID: {} успешно изменён", userId);
        return UserMapper.toUserDto(userStorage.update(userId, userOld));
    }

    @Override
    public void delete(Long userId) {
        containUserId(userId);
        log.debug("Пользователь с ID {} успешно удалён", userId);
        userStorage.delete(userId);
    }

    private boolean containUserName(User userN) {
        return userStorage.get().stream().anyMatch(user -> user.getName().equals(userN.getName()));
    }

    private boolean containUserEmail(User userE){
        return userStorage.get().stream().anyMatch(user -> user.getEmail().equals(userE.getEmail()));
    }

    /**
     * Метод проверки присутствия пользователя на сервере
     */
    private void containUserId(long userId) {
        //if (userStorage.get().stream().noneMatch(user -> user.getId() == userId)) {
        if (!userStorage.isContainUserId(userId)) {
            throw new ObjectUnknownException("Пользователь с ID " + userId + " не существует");
        }
    }
}
