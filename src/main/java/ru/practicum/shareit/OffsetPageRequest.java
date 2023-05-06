package ru.practicum.shareit;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * Класс выдачи собственного постраничного результата
 */
public class OffsetPageRequest extends PageRequest {
    private final Integer from;

    public OffsetPageRequest(Integer from, Integer size, Sort sort) {
        super(from / size, size, sort);
        this.from = from;
    }

    @Override
    public long getOffset() {
        return from;
    }
}
