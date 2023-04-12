package ru.practicum.shareit.exceptions;

/**
 * Класс собственного исключения при работе с объектом (который не найден)
 */
public class ObjectUnknownException extends RuntimeException {
    public ObjectUnknownException(String message) {
        super(message);
    }
}
