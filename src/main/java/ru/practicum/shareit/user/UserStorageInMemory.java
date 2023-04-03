package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserStorageInMemory implements UserStorage {

    private final List<User> userList = new ArrayList<>();
    private Long id = 1L;

    /**
     * Генератор ID
     *
     * @return ID
     */
    private Long generatorId() {
        return id++;
    }

    @Override
    public List<User> get() {
        return userList;
    }

    @Override
    public User create(User user) {
        user.setId(generatorId());
        userList.add(user);
        return user;
    }
}
