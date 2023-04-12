package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.models.dto.UserDto;
import ru.practicum.shareit.user.services.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * Класс UserController по энпоинту Users
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Метод (эндпоинт) получения списка всех пользователей
     *
     * @return Список UserDto
     */
    @GetMapping()
    public List<UserDto> get() {
        return userService.get();
    }

    /**
     * Метод (эндпоинт) получения объекта UserDto по ID пользователя
     *
     * @param userId ID пользователя
     * @return Объект UserDto
     */
    @GetMapping("/{userId}")
    public UserDto get(@PathVariable Long userId) {
        return userService.get(userId);
    }

    /**
     * Метод (эндпоинт) создания объекта User
     *
     * @param userDto Объект UserDto
     * @return Созданный объект UserDto
     */
    @PostMapping()
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        return userService.create(userDto);
    }

    /**
     * Метод (эндпоинт) обновления User по ID пользователя
     *
     * @param userId  ID пользователя
     * @param userDto Объект UserDto
     * @return Обновлённый объект UserDto
     */
    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable Long userId, @RequestBody UserDto userDto) {
        return userService.update(userId, userDto);
    }

    /**
     * Метод (эндпоинт) удаления объекта User
     *
     * @param userId ID пользователя
     */
    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        userService.delete(userId);
    }


}
