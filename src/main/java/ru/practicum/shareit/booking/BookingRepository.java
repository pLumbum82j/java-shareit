package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.models.Booking;
import ru.practicum.shareit.exceptions.ObjectUnknownException;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    default Booking get(long id) {
        return findById(id).orElseThrow(() -> new ObjectUnknownException("Бронирование с ID: " + id + " не существует"));
    }
    @Query("SELECT b FROM Booking b WHERE b.booker.id = ?1 ORDER BY b.start DESC")
    List<Booking> getAll(long bookerId);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = ?1 AND b.start <= ?2 AND b.end >= ?2 ORDER BY b.start DESC")
    List<Booking> getAllCurrent(long bookerId, LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = ?1 AND b.end < ?2 ORDER BY b.start DESC")
    List<Booking> getAllPast(long bookerId, LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = ?1 AND b.start > ?2 ORDER BY b.start DESC")
    List<Booking> getAllFuture(long bookerId, LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = ?1 AND b.status = ?2 ORDER BY b.start DESC")
    List<Booking> getAllByStatus(long bookerId, BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = ?1 ORDER BY b.start DESC")
    List<Booking> getAllOwner(long bookerId);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = ?1 AND b.start <= ?2 AND b.end >= ?2 ORDER BY b.start DESC")
    List<Booking> getAllCurrentOwner(long bookerId, LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = ?1 AND b.end < ?2 ORDER BY b.start DESC")
    List<Booking> getAllPastOwner(long bookerId, LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = ?1 AND b.start > ?2 ORDER BY b.start DESC")
    List<Booking> getAllFutureOwner(long bookerId, LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = ?1 AND b.status = ?2 ORDER BY b.start DESC")
    List<Booking> getAllByStatusOwner(long bookerId, BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.item.id = ?1 AND b.end < ?2 ORDER BY b.end DESC")
    Booking getLastForItem(long itemId, LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.item.id = ?1 AND b.start > ?2 ORDER BY b.start ASC")
    Booking getNextForItem(long itemId, LocalDateTime now);

    @Query(
            "SELECT COUNT (b) FROM Booking b " +
                    "WHERE b.booker.id = ?1 " +
                    "AND b.item.id = ?2 " +
                    "AND b.end < ?3 " +
                    "AND b.status = ru.practicum.shareit.booking.BookingStatus.APPROVED"
    )
    Integer getFinishedCount(long userId, long itemId, LocalDateTime now);
}

