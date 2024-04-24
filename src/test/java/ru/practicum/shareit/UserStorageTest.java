package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.user.storage.UserStorageImpl;

import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserStorageTest {

    private UserStorage userStorage;


    @BeforeEach
    void init() {
        userStorage = new UserStorageImpl();
    }

    @Test
    public void create() {
        assertThatCode(() -> {
            User newUser = User.builder()
                    .id(1L)
                    .name("Name")
                    .email("email")
                    .build();
            userStorage.create(newUser);
        }).doesNotThrowAnyException();
    }

}
