package ru.practicum.shareit.user;

import java.util.List;
import java.util.Optional;

public interface IUserStorage {
    User add(User user);

    User getId(Long user);

    User delete(User user);

    User update(User user);

    List<User> getUsers();

    boolean isContains(User user);

    Optional<Long> getUserByEmail(String email);
}
