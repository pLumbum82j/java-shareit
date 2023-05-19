package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

/**
 * Класс UserController по энпоинту Users
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {


    private final UserClient userClient;

    /**
     * Метод (эндпоинт) получения списка всех пользователей
     *
     * @return Список UserDto
     */
    @GetMapping()
    public ResponseEntity<Object> getAllUsers() {
        log.info("Get all users");
        return userClient.get();
    }

    /**
     * Метод (эндпоинт) получения объекта UserDto по ID пользователя
     *
     * @param userId ID пользователя
     * @return Объект UserDto
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable Long userId) {
        log.info("Get user {}", userId);
        return userClient.get(userId);
    }

    /**
     * Метод (эндпоинт) создания объекта User
     *
     * @param userDto Объект UserDto
     * @return Созданный объект UserDto
     */
    @PostMapping()
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserDto userDto) {
        log.info("Creating booking {}", userDto);
        return userClient.createUser(userDto);

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
        log.info("Update user with id={},", userDto.getId());
        return userClient.update(userId, userDto);
    }

    /**
     * Метод (эндпоинт) удаления объекта User
     *
     * @param userId ID пользователя
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> delete(@PathVariable Long userId) {
        log.info("Delete user with id={}", userId);
        return userClient.delete(userId);
    }
}
