package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserDto;

@Slf4j
@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    /**
     * Метод получения списка всех пользователей
     *
     * @return Список UserDto
     */
    public ResponseEntity<Object> get() {
        log.debug("Получен запрос на список всех пользователей");
        return get("");
    }

    /**
     * Метод получения объекта UserDto по ID пользователя
     *
     * @param userId ID пользователя
     * @return Объект UserDto
     */
    public ResponseEntity<Object> get(long userId) {
        log.debug("Получен запрос на поиск пользователя по ID: {}", userId);
        return get("/" + userId);
    }

    /**
     * Метод создания объекта User
     *
     * @param userDto Объект UserDto
     * @return Созданный объект UserDto
     */
    public ResponseEntity<Object> create(UserDto userDto) {
        log.debug("Получен запрос на создание пользователя с именем: {}", userDto.getName());
        return post("", userDto);
    }

    /**
     * Метод обновления User по ID пользователя
     *
     * @param userId  ID пользователя
     * @param userDto Объект UserDto
     * @return Обновлённый объект UserDto
     */
    public ResponseEntity<Object> update(Long userId, UserDto userDto) {
        log.debug("Получен запрос на обновление пользователя с ID: {}", userId);
        return patch("/" + userId, userDto);
    }

    /**
     * Метод удаления объекта User
     *
     * @param userId ID пользователя
     */
    public ResponseEntity<Object> delete(Long userId) {
        log.debug("Получен запрос на удаления пользователя с ID {}", userId);
        return delete("/" + userId);
    }
}
