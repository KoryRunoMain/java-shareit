package ru.practicum.shareit.userTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {

    @InjectMocks
    private UserMapper userMapper;

    private static final Long USER_ID = 1L;

    private final User user = new User(USER_ID, "user", "user@user.user");
    private final UserDto userDto = new UserDto(USER_ID, "user", "user@user.user");

    @Test
    void test_1_toUser_And_ReturnOk() {
        User actUser = userMapper.toUser(userDto);
        assertEquals(actUser.getId(), user.getId());
        assertEquals(actUser.getName(), user.getName());
        assertEquals(actUser.getEmail(), user.getEmail());
    }

    @Test
    void test_2_toUserDto_And_ReturnOk() {
        UserDto actUserDto = userMapper.toUserDto(user);
        assertEquals(actUserDto.getId(), userDto.getId());
        assertEquals(actUserDto.getName(), userDto.getName());
        assertEquals(actUserDto.getEmail(), userDto.getEmail());
    }

}
