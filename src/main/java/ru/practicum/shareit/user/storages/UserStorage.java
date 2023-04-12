package ru.practicum.shareit.user.storages;

import ru.practicum.shareit.user.models.User;

import java.util.List;

/**
 * Интерфейс UserStorage для работы с данными User на сервере
 */
public interface UserStorage {
    /**
     * Метод получения списка всех пользователей
     *
     * @return Список User
     */
    List<User> get();

    /**
     * Метод получения объекта User по ID пользователя
     *
     * @param userId ID пользователя
     * @return Объект User
     */
    User get(Long userId);

    /**
     * Метод создания объекта User
     *
     * @param user Объект User
     * @return Созданный объект User
     */
    User create(User user);

    /**
     * Метод обновления User по ID пользователя
     *
     * @param userId ID пользователя
     * @param user   Объект User
     * @return Обновлённый объект User
     */
    User update(Long userId, User user);

    /**
     * Метод удаления объекта User
     *
     * @param userId ID пользователя
     */
    void delete(Long userId);

    /**
     * Метод проверки пользователя на сервере по его ID
     *
     * @param userId ID пользователя
     */
    boolean isContainUserId(Long userId);
}
