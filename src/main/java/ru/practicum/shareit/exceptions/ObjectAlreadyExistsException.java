package ru.practicum.shareit.exceptions;

/**
 * Класс собственного исключения при работе с объектом (который уже существует)
 */
public class ObjectAlreadyExistsException extends RuntimeException {
    public ObjectAlreadyExistsException(String message) {
        super(message);
    }
}
