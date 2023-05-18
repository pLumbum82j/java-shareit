package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

/**
 * Класс UserController по энпоинту Users
 */
@RestController
@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {


    private final UserClient userClient;

//    /**
//     * Метод (эндпоинт) получения списка всех пользователей
//     *
//     * @return Список UserDto
//     */
//    @GetMapping()
//    public List<UserDto> get() {
//        return userService.get();
//    }

    /**
     * Метод (эндпоинт) получения объекта UserDto по ID пользователя
     *
     * @param userId ID пользователя
     * @return Объект UserDto
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable Long userId) {
        log.info("Get user {}", userId);
        return userClient.getUser(userId);
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
//
//    /**
//     * Метод (эндпоинт) обновления User по ID пользователя
//     *
//     * @param userId  ID пользователя
//     * @param userDto Объект UserDto
//     * @return Обновлённый объект UserDto
//     */
//    @PatchMapping("/{userId}")
//    public UserDto update(@PathVariable Long userId, @RequestBody UserDto userDto) {
//        return userService.update(userId, userDto);
//    }
//
//    /**
//     * Метод (эндпоинт) удаления объекта User
//     *
//     * @param userId ID пользователя
//     */
//    @DeleteMapping("/{userId}")
//    public void delete(@PathVariable Long userId) {
//        userService.delete(userId);
//    }
}
