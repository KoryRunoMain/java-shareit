package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;
import java.util.List;
import java.util.Optional;

public interface UserStorage {

    User create(User user);

    User update(User user);

    User get(Long id);

    User delete(Long id);

    List<User> getUsers();

    boolean isContains(Long user);

    Optional<Long> getUserByEmail(String email);

}
