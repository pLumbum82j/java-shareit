package ru.practicum.shareit.booking;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.practicum.shareit.item.models.Item;
import ru.practicum.shareit.user.models.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString(exclude = {"item", "booker"})
@Entity
@Table(name = "bookings")
@NoArgsConstructor

public class Booking {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // — уникальный идентификатор бронирования;
    @Column(name = "start_date")
    private LocalDateTime start; // — дата и время начала бронирования;
    @Column(name = "end_date")
    private LocalDateTime end; // — дата и время конца бронирования;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item; // — вещь, которую пользователь бронирует;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "booker_id", referencedColumnName = "id")
    private User booker; // — пользователь, который осуществляет бронирование;
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.WAITING; // — статус бронирования.
}