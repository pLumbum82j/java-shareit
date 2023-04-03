package ru.practicum.shareit.user;

import org.springframework.web.bind.annotation.*;

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
    public List<User> get() {
        return userService.get();
    }

    /**
     * Метод (эндпоинт) создания пользователя
     *
     * @param user Принятый объект пользователя по эндпоинту
     * @return созданный объект пользователя
     */
    @PostMapping()
    public User create(@RequestBody User user) {
        return userService.create(user);
    }

}
