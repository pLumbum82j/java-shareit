package ru.practicum.shareit.user.services;

import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.shareit.user.models.User;
import ru.practicum.shareit.user.models.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> get();
    UserDto get(@PathVariable Long userId);
    UserDto create(UserDto userDto);
}
