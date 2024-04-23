package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class UserStorage implements IUserStorage {
    private final Map<Long, User> users;
    private Long id;

    public UserStorage() {
        id = 0L;
        users = new HashMap<>();
    }

    public User create(User user) {
        user.setId(++id);
        return users.put(user.getId(), user);
    }

    @Override
    public User get(Long id) {
        return users.get(id);
    }

    @Override
    public User delete(Long id) {
        return users.remove(id);
    }

    @Override
    public User update(User user) {
        return users.put(user.getId(),user);
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public boolean isContains(User user) {
        return users.containsKey(user.getId());
    }

    @Override
    public Optional<Long> getUserByEmail(String email) {
        return users.values().stream()
                .filter(user -> email.equals(user.getEmail()))
                .map(User::getId)
                .findFirst();
    }
}
