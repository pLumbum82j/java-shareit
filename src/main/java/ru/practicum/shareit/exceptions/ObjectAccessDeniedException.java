package ru.practicum.shareit.exceptions;

/**
 * Класс собственного исключения при работе с объектом (отказ в доступе)
 */
public class ObjectAccessDeniedException extends RuntimeException {
    public ObjectAccessDeniedException(String message) {
        super(message);
    }
}
