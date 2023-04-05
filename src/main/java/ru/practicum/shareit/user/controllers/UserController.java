package ru.practicum.shareit.user.controllers;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.models.dto.UserDto;
import ru.practicum.shareit.user.services.UserService;
import ru.practicum.shareit.user.models.User;

import javax.validation.Valid;
import java.util.List;

/**
 * Класс Контроллер по энпоинту Users
 */
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Метод (эндпоинт) получения списка пользователей
     *
     * @return Список пользователей
     */
    @GetMapping()
    public List<UserDto> get() {
        return userService.get();
    }

    @GetMapping("/{userId}")
    public UserDto get(@PathVariable Long userId) {
        return userService.get(userId);
    }

    /**
     * Метод (эндпоинт) создания пользователя
     *
     * @param userDto Принятый объект пользователя по эндпоинту
     * @return созданный объект пользователя
     */
    @PostMapping()
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        return userService.create(userDto);
    }

}
