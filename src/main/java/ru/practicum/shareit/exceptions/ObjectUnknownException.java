package ru.practicum.shareit.exceptions;

/**
 * Класс собственного исключения при работе с искомым объектом (который не существует)
 */
public class ObjectUnknownException extends RuntimeException {
    public ObjectUnknownException(String message) {
        super(message);
    }
}
