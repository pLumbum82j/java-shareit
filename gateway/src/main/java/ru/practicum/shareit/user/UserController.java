package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

/**
 * Класс UserController по энпоинту Users
 */
@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {


    private final UserClient userClient;

    /**
     * Метод (эндпоинт) получения списка всех пользователей
     *
     * @return Список UserDto
     */
    @GetMapping()
    public ResponseEntity<Object> get() {
        return userClient.get();
    }

    /**
     * Метод (эндпоинт) получения объекта UserDto по ID пользователя
     *
     * @param userId ID пользователя
     * @return Объект UserDto
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Object> get(@PathVariable Long userId) {
        return userClient.get(userId);
    }

    /**
     * Метод (эндпоинт) создания объекта User
     *
     * @param userDto Объект UserDto
     * @return Созданный объект UserDto
     */
    @PostMapping()
    public ResponseEntity<Object> create(@RequestBody @Valid UserDto userDto) {
        return userClient.create(userDto);

    }

    /**
     * Метод (эндпоинт) обновления User по ID пользователя
     *
     * @param userId  ID пользователя
     * @param userDto Объект UserDto
     * @return Обновлённый объект UserDto
     */
    @PatchMapping(path = "/{userId}")
    public ResponseEntity<Object> update(@PathVariable("userId") Long userId, @RequestBody UserDto userDto) {
        return userClient.update(userId, userDto);
    }

    /**
     * Метод (эндпоинт) удаления объекта User
     *
     * @param userId ID пользователя
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> delete(@PathVariable Long userId) {
        return userClient.delete(userId);
    }
}
