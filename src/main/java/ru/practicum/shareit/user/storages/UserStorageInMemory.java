package ru.practicum.shareit.user.storages;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserStorageInMemory implements UserStorage {

    private final Map<Long, User> userList = new HashMap<>();
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
        return new ArrayList<>(userList.values());
    }


    @Override
    public User get(Long userId) {
        return userList.get(userId);
    }

    @Override
    public User create(User user) {
        user.setId(generatorId());
        userList.put(user.getId(), user);
        return user;
    }


}
