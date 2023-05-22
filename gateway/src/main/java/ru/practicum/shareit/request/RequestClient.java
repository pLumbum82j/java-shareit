package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Map;

@Slf4j
@Service
public class RequestClient extends BaseClient {

    private static final String API_PREFIX = "/requests";

    @Autowired
    public RequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    /**
     * Метод получения списка запросов по пользователю
     *
     * @param userId ID пользователя
     * @return Список объектов ItemRequestDto
     */
    public ResponseEntity<Object> get(Long userId) {
        log.debug("Получен запрос на список itemRequest по userId: {}", userId);
        return get("", userId);
    }

    /**
     * Метод получения списка всех запросов постранично
     *
     * @param userId ID пользователя
     * @param from   индекс первого элемента
     * @param size   количество элементов для отображения
     * @return Список объектов ItemRequestDto
     */
    public ResponseEntity<Object> get(Integer from, Integer size, Long userId) {
        Map<String, Object> parameters = Map.of("from", from, "size", size);
        log.debug("Получен запрос на список itemRequest по userId: {}, size: {}, from: {}", userId, size, from);
        return get("/all?from={from}&size={size}", userId, parameters);
    }

    /**
     * Метод (эндпоинт) получения одного запроса по пользователю
     *
     * @param userId    ID пользователя
     * @param requestId ID запроса
     * @return Объект ItemRequestDto
     */
    public ResponseEntity<Object> get(Long requestId, Long userId) {
        log.debug("Получен запрос на получение itemRequest по userId: {}", userId);
        return get("/" + requestId, userId);
    }

    /**
     * Метод (эндпоинт) создания запроса
     *
     * @param userId         ID пользователя
     * @param itemRequestDto Объект itemRequestDto
     * @return Созданный объект itemRequestDto
     */
    public ResponseEntity<Object> create(Long userId, ItemRequestDto itemRequestDto) {
        log.debug("Получен запрос на создание itemRequest по userId: {}", userId);
        return post("", userId, itemRequestDto);
    }
}