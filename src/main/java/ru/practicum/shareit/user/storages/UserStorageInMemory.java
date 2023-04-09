package ru.practicum.shareit.user.storages;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Клас UserStorageInMemory для хранения User в оперативной памяти сервера
 */
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
        if (user.getId() == null) {
            user.setId(generatorId());
        }
        userList.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(Long userId, User user) {
        user.setId(userId);
        userList.replace(userId, user);
        return user;
    }

    @Override
    public void delete(Long userId) {
        userList.remove(userId);
    }

    @Override
    public boolean isContainUserId(Long userId) {
        return userList.containsKey(userId);
    }

}
