package ru.practicum.shareit.exceptions;

/**
 * Класс собственного исключения при работе с объектом (отказ в доступе)
 */
public class UnknownStatusException extends RuntimeException {
    public UnknownStatusException(String message) {
        super(message);
    }
}
