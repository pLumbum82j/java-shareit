package ru.practicum.shareit.booking;

/**
 * ENUM состояния бронирования
 */
public enum BookingState {
    ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED;

    /**
     * Статический метод проверки состояния бронирования
     *
     * @param state входящий статус
     * @return Проверенный статус (если статус неправильный возвращает null) для дальнейшей обработки
     */
    public static BookingState from(String state) {
        for (BookingState value : BookingState.values()) {
            if (value.name().equals(state)) {
                return value;
            }
        }
        return null;
    }

}
