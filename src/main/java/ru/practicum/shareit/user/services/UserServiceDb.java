package ru.practicum.shareit.user.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.models.User;
import ru.practicum.shareit.user.models.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceDb implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> get() {
        log.debug("Получен запрос на список всех пользователей");
        return userRepository.findAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto get(Long userId) {
        log.debug("Получен запрос на поиск пользователя по ID: {}", userId);
        return UserMapper.toUserDto(userRepository.get(userId));
    }

    @Override
    public UserDto create(UserDto userDto) {
        User userTemp = userRepository.save(UserMapper.toUser(userDto));
        log.debug("Получен запрос на создание пользователя с именем: {}", userTemp.getName());
        return UserMapper.toUserDto(userTemp);
    }

    @Override
    public UserDto update(Long userId, UserDto userDto) {
        User userOld = userRepository.get(userId);
        User userTemp = UserMapper.toUser(userDto);
//        if (containUserName(userTemp) && !userTemp.getName().equals(userOld.getName())) {
//            throw new ObjectAlreadyExistsException("Пользователь с name: " + userTemp.getName() + " уже существует");
//        }
//        if (containUserEmail(userTemp) && !userTemp.getEmail().equals(userOld.getEmail())) {
//            throw new ObjectAlreadyExistsException("Пользователь с email: " + userTemp.getEmail() + " уже существует");
//        }
        if (userTemp.getEmail() != null && !userTemp.getEmail().isEmpty()) {
            userOld.setEmail(userTemp.getEmail());
        }
        if (userTemp.getName() != null && !userTemp.getName().isEmpty()) {
            userOld.setName(userTemp.getName());
        }
        log.debug("Получен запрос на обновление пользователя с ID: {}", userId);
        return UserMapper.toUserDto(userRepository.save(userOld));
    }

    @Override
    public void delete(Long userId) {
        log.debug("Получен запрос на удаления пользователя с ID {}", userId);
        userRepository.deleteById(userId);
    }
}
