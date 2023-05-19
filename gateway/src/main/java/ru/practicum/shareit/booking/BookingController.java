package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	private static final String SHARER = "X-Sharer-User-Id";

	/**
	 * Метод (эндпоинт) получения бронирования
	 *
	 * @param bookingId ID бронирования
	 * @param userId    ID пользователя
	 * @return Объект BookingDto
	 */
	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> get(@RequestHeader(SHARER) long userId,
											 @PathVariable Long bookingId) {
		log.info("Get booking {}, userId={}", bookingId, userId);
		return bookingClient.get(userId, bookingId);
	}

	/**
	 * Метод (эндпоинт) получения списка бронирований по пользователю
	 *
	 * @param userId ID пользователя
	 * @param stateParam  Статус
	 * @param from   индекс первого элемента
	 * @param size   количество элементов для отображения
	 * @return Список объектов BookingDto
	 */
	@GetMapping
	public ResponseEntity<Object> getUserBookings(@RequestHeader(SHARER) long userId,
			@RequestParam(name = "state", defaultValue = "all") String stateParam,
			@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
			@Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
		return bookingClient.getUserBookings(userId, state, from, size);
	}

	/**
	 * Метод (эндпоинт) получения списка бронирований по владельцу
	 *
	 * @param ownerId ID владельца
	 * @param stateParam   Статус
	 * @param from    индекс первого элемента
	 * @param size    количество элементов для отображения
	 * @return Список объектов BookingDto
	 */
	@GetMapping("/owner")
	public ResponseEntity<Object> getOwnerBookings(@RequestHeader(SHARER) long ownerId,
											 @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
											 @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
											 @RequestParam(defaultValue = "10") @Positive Integer size) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		log.info("Get booking by Owner with state {}, ownerId={}, from={}, size={}", stateParam, ownerId, from, size);
		return bookingClient.getOwnerBookings(ownerId, state, from, size);
	}

	/**
	 * Метод (эндпоинт) создания бронирования
	 *
	 * @param requestDto Объект BookingDto
	 * @param userId     ID пользователя
	 * @return Созданный BookingDto
	 */
	@PostMapping
	public ResponseEntity<Object> create(@RequestHeader(SHARER) long userId,
			@RequestBody @Valid BookItemRequestDto requestDto) {
		log.info("Creating booking {}, userId={}", requestDto, userId);
		return bookingClient.create(userId, requestDto);
	}

	/**
	 * Метод обновления бронирования
	 *
	 * @param bookingId ID бронирования
	 * @param approved  Обновляемый статус
	 * @param userId    ID пользователя
	 * @return Обновлённый BookingDto
	 */
	@PatchMapping("/{bookingId}")
	ResponseEntity<Object>  update(@PathVariable long bookingId,
							 @RequestParam("approved") String approved,
							 @RequestHeader(SHARER) long userId) {
		log.info("Get a booking approval request with parameters approved={}, ownerId={}, bookingId={}",
				approved,
				userId,
				bookingId);
		return bookingClient.update(userId, bookingId, approved);
	}
}
