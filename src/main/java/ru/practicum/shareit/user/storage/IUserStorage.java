package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

@Component
public interface IUserStorage {
    User create(User user);

    User update(User user);

    User get(Long id);

    User delete(Long id);

    List<User> getUsers();

    boolean isContains(User user);

    Optional<Long> getUserByEmail(String email);
}
