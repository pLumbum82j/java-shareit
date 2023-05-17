package ru.practicum.shareit.user.services;

import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.shareit.user.models.dto.UserDto;

import java.util.List;

/**
 * Интерфейс UserService для обработки логики запросов из ItemController
 */
public interface UserService {
    /**
     * Метод получения списка всех пользователей
     *
     * @return Список UserDto
     */
    List<UserDto> get();

    /**
     * Метод получения объекта UserDto по ID пользователя
     *
     * @param userId ID пользователя
     * @return Объект UserDto
     */
    UserDto get(@PathVariable Long userId);

    /**
     * Метод создания объекта User
     *
     * @param userDto Объект UserDto
     * @return Созданный объект UserDto
     */
    UserDto create(UserDto userDto);

    /**
     * Метод обновления User по ID пользователя
     *
     * @param userId  ID пользователя
     * @param userDto Объект UserDto
     * @return Обновлённый объект UserDto
     */
    UserDto update(Long userId, UserDto userDto);

    /**
     * Метод удаления объекта User
     *
     * @param userId ID пользователя
     */
    void delete(Long userId);
}
