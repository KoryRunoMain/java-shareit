package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class UserStorage implements IUserStorage {
    private final Map<Long, User> users;
    private Long id;

    public UserStorage(User user, Long id) {
        id = 0L;
        users = new HashMap<>();
    }

    public User add(User user) {
        user.setId(++id);
        users.put(user.getId(), user);
        log.info("add: userId = {}", user.getId());
        return user;
    }

    @Override
    public User getId(Long user) {
        return users.get(user);
    }

    @Override
    public User delete(User user) {
        User deleteUser = users.remove(user.getId());
        log.info("delete UserId {}", deleteUser.getId());
        return deleteUser;
    }

    @Override
    public User update(User user) {
        User updateUser = users.put(user.getId(),user);
        assert updateUser != null;
        log.info("update UserId {}", updateUser.getId());
        return updateUser;
    }

    @Override
    public List<User> getUsers() {
        List<User> getUsers = new ArrayList<>(users.values());
        log.info("get User List {}", getUsers);
        return getUsers;
    }

    @Override
    public boolean isContains(User user) {
        boolean isContainsUser = users.containsKey(user.getId());
        log.info("isCont user? {}", isContainsUser);
        return isContainsUser;
    }

    @Override
    public Optional<Long> getUserByEmail(String email) {
        Optional<Long> userEmail = users.values().stream()
                .filter(user -> email.equals(user.getEmail()))
                .map(User::getId)
                .findFirst();
        log.info("getUser email {}", userEmail);
        return userEmail;
    }
}
