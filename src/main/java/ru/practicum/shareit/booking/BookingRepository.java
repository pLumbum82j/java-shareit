package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.models.Booking;
import ru.practicum.shareit.exceptions.ObjectUnknownException;
import ru.practicum.shareit.item.models.Item;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Интерфейс BookingRepository для обработки запросов к БД
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    default Booking get(long id) {
        return findById(id).orElseThrow(() -> new ObjectUnknownException("Бронирование с ID: " + id + " не существует"));
    }

    List<Booking> findAllByBookerId(Long bookerId, Sort sort);

    List<Booking> findAllByBookerIdAndStartIsBeforeAndEndIsAfter(Long bookerId, LocalDateTime start, LocalDateTime end, Sort sort);

    List<Booking> findAllByBookerIdAndEndIsBefore(Long bookerId, LocalDateTime start, Sort sort);

    List<Booking> findAllByBookerIdAndStartIsAfter(Long bookerId, LocalDateTime start, Sort sort);

    List<Booking> findAllByBookerIdAndStatus(Long bookerId, BookingStatus status, Sort sort);

    List<Booking> findAllByItemOwnerId(Long bookerId, Sort sort);

    List<Booking> findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfter(Long bookerId, LocalDateTime start, LocalDateTime end, Sort sort);

    List<Booking> findAllByItemOwnerIdAndEndIsBefore(Long bookerId, LocalDateTime start, Sort sort);

    List<Booking> findAllByItemOwnerIdAndStartIsAfter(Long bookerId, LocalDateTime start, Sort sort);

    List<Booking> findAllByItemOwnerIdAndStatus(Long bookerId, BookingStatus status, Sort sort);

    List<Booking> findAllByItemInAndStatus(List<Item> items, BookingStatus status);

    List<Booking> findAllByBookerIdAndItemIdAndStatusAndEndBefore(Long userId, Long itemId, BookingStatus status, LocalDateTime end);

}

