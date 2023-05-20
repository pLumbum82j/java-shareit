package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.exceptions.BadRequestException;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    /**
     * Метод получения объекта BookingDto по bookingId и userId
     *
     * @param bookingId ID бронирования
     * @param userId    ID пользователя
     * @return Объект BookingDto
     */
    public ResponseEntity<Object> get(Long userId, Long bookingId) {
        log.debug("Получен запрос на поиск бронирования по bookingId: {}, userId: {}", bookingId, userId);
        return get("/" + bookingId, userId);
    }

    /**
     * Метод получения списка BookingDto по userId и state
     *
     * @param userId     ID пользователя
     * @param stateParam Статус
     * @param from       индекс первого элемента
     * @param size       количество элементов для отображения
     * @return Список объектов BookingDto
     */
    public ResponseEntity<Object> getUserBookings(Long userId, String stateParam, Integer from, Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new BadRequestException("Unknown state: " + stateParam));
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        log.debug("Получен запрос список бронирования по state: {}, userId: {}", state, userId);
        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }

    /**
     * Метод получения списка BookingDto по ownerId и state
     *
     * @param ownerId    ID владельца
     * @param stateParam Статус
     * @param from       индекс первого элемента
     * @param size       количество элементов для отображения
     * @return Список объектов BookingDto
     */
    public ResponseEntity<Object> getOwnerBookings(Long ownerId, String stateParam, Integer from, Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new BadRequestException("Unknown state: " + stateParam));
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        log.debug("Получен запрос список бронирования по state: {}, ownerId: {}", state, ownerId);
        return get("/owner?state={state}&from={from}&size={size}", ownerId, parameters);
    }

    /**
     * Метод создания объекта Booking
     *
     * @param requestDto Объект BookingDto
     * @param userId     ID пользователя
     * @return Созданный объект BookingDto
     */
    public ResponseEntity<Object> create(Long userId, BookItemRequestDto requestDto) {
        log.debug("Получен запрос создание бронирования userId: {}, itemId: {}", userId, requestDto.getItemId());
        if (requestDto.getStart() == null || requestDto.getEnd() == null
                || requestDto.getStart().isAfter(requestDto.getEnd())
                || requestDto.getStart().isEqual(requestDto.getEnd())
                || requestDto.getStart().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Ошибка во времени Start/End time");
        }
        return post("", userId, requestDto);
    }

    /**
     * Метод обновления статуса Booking по bookingId, approved, userId
     *
     * @param bookingId ID бронирования
     * @param approved  Обновляемый статус
     * @param userId    ID пользователя
     * @return Обновлённый объект BookingDto
     */
    public ResponseEntity<Object> update(Long bookingId, Long userId, boolean approved) {
        Map<String, Object> parameters = Map.of("approved", approved
        );
        log.debug("Получен запрос обновление бронирования bookingId: {}, approved {}, userId: {}",
                bookingId, approved, userId);
        return patch("/" + bookingId + "?approved={approved}", userId, parameters);
    }
}
