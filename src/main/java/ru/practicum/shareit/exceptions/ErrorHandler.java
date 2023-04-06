package ru.practicum.shareit.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    public static final String ERROR = "error";

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) //400
    public Map<String, String> handleArgumentNotValidEx(final MethodArgumentNotValidException e) {
        log.warn("Ошибка валидации.");
        return Map.of(ERROR, e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleAlreadyExist(final ObjectAlreadyExistsException e) {
        log.debug(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) //400
    public Map<String, String> handleObjectUnknownEx(final ObjectUnknownException e) {
        log.warn("Ошибка, Запрашиваемый объект не найден.");
        return Map.of(ERROR, e.getMessage());
    }

//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.BAD_REQUEST) //400
//    public Map<String, String> handleObjectAlreadyExistsEx(final ObjectAlreadyExistsException e) {
//        log.warn("Ошибка, Запрашиваемый объект уже существует.");
//        return Map.of(ERROR, e.getMessage());
//    }

    @ExceptionHandler({IllegalArgumentException.class, NullPointerException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleNullOrIllegalArgumentEx(final RuntimeException e) {
        log.warn("Сервер столкнулся с неожиданной ошибкой, которая помешала выполнить запрос.");
        return Map.of(ERROR, e.getMessage());
    }
}