package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class UserStorageImpl implements UserStorage {
    private Map<Long, User> users;
    private Long id;

    public UserStorageImpl() {
        id = 0L;
        users = new HashMap<>();
    }

    private Long createId() {
        return ++id;
    }

    @Override
    public User create(User user) {
        Long id = createId();
        user.setId(id);
        users.put(id, user);
        return user;
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
       users.put(user.getId(),user);
        return user;
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public boolean isContains(Long userId) {
        return users.containsKey(userId);
    }

    @Override
    public Optional<Long> getUserByEmail(String email) {
        return users.values().stream()
                .filter(user -> email.equals(user.getEmail()))
                .map(User::getId)
                .findFirst();
    }
}
