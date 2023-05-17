package ru.practicum.shareit.exceptions;

/**
 * Класс собственного исключения при работе с объектом (который недоступен)
 */
public class ObjectAvailabilityDenyException extends RuntimeException {
    public ObjectAvailabilityDenyException(String message) {
        super(message);
    }

}
