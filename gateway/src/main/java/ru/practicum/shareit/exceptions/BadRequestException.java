package ru.practicum.shareit.exceptions;

/**
 * Класс собственного исключения при неправильном запросе
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}