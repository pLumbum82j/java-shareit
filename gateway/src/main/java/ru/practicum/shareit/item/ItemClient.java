package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    /**
     * Метод получения списка ItemDto по ID пользователя
     *
     * @param userId ID пользователя
     * @return Список ItemDto
     */
    public ResponseEntity<Object> get(Long userId) {
        log.debug("Получен запрос на список ItemDto по userId {}", userId);
        return get("", userId);
    }

    /**
     * Метод получения ItemDto по ID вещи с проверкой ID пользователя
     *
     * @param userId ID пользователя
     * @param itemId ID вещи
     * @return Объект ItemDto
     */
    public ResponseEntity<Object> get(Long userId, Long itemId) {
        log.debug("Получен запрос на ItemDto по itemId: {} и userId: {}", itemId, userId);
        return get("/" + itemId, userId);
    }

    /**
     * Метод получения списка ItemDto с использованием поиска по тексту и проверкой ID пользователя
     *
     * @param userId ID пользователя
     * @param text   Текст поиска
     * @param from   индекс первого элемента
     * @param size   количество элементов для отображения
     * @return Список ItemDto
     */
    public ResponseEntity<Object> search(String text, Integer from, Integer size, Long userId) {
        if (text == null || text.isEmpty()) {
            log.debug("Получен запрос на список ItemDto по text: {} - ничего не найдено", text);
            return ResponseEntity.status(HttpStatus.OK).body(List.of());
        }
        Map<String, Object> parameters = Map.of(
                "text", text,
                "from", from,
                "size", size
        );
        log.debug("Получен запрос на список ItemDto по text: {} - найдены совпадения по тексту", text);
        return get("/search?text={text}&from={from}&size={size}", userId, parameters);
    }

    /**
     * Метод создания Item
     *
     * @param userId  ID пользователя
     * @param itemDto Объект ItemDto
     * @return Созданный ItemDto
     */
    public ResponseEntity<Object> create(Long userId, ItemDto itemDto) {
        log.debug("Получен запрос на создание Item по userId: {}", userId);
        return post("", userId, itemDto);
    }

    /**
     * Метод создания комментария к вещи
     *
     * @param commentDto Объект комментария
     * @param itemId     ID вещи
     * @param userId     ID пользователя
     * @return Созданный CommentDto
     */
    public ResponseEntity<Object> create(Long userId, Long itemId, CommentDto commentDto) {
        log.debug("Получен запрос на создание Comment по userId: {}, itemId {}", userId, itemId);
        return post("/" + itemId + "/comment", userId, commentDto);
    }

    /**
     * Метод обновления Item
     *
     * @param userId  ID пользователя
     * @param itemId  ID вещи
     * @param itemDto Объект ItemDto
     * @return Обновлённый ItemDto
     */
    public ResponseEntity<Object> update(Long userId, Long itemId, ItemDto itemDto) {
        log.debug("Получен запрос на изменение вещи с ID: {}", itemId);
        return patch("/" + itemId, userId, itemDto);
    }
}