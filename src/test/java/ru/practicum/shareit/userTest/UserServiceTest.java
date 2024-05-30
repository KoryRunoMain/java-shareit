package ru.practicum.shareit.userTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.practicum.shareit.exception.AlreadyExistsException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceTest {

    @Mock
    private UserMapper mapper;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private static final Long USER_ID = 1L;
    private static final Long WRONG_ID = 5L;

    //User
    private final User user = new User(USER_ID, "user", "user@user.user");
    private final User wrongUser = new User(WRONG_ID, null, null);
    private final UserDto userDto = new UserDto(USER_ID, "user", "user@user.user");
    private final UserDto updatedUserDto = new UserDto(USER_ID, "updatedUser", "user@user.user");

    @BeforeEach
    void setUp() {
        lenient().when(mapper.toUserDto(any(User.class))).thenReturn(userDto);
        lenient().when(mapper.toUser(any(UserDto.class))).thenReturn(user);
    }

    @Test
    void getById_successfullyGet() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        assertEquals(userDto, userService.getById(USER_ID));
    }

    @Test
    void getById_notFoundUserId() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.getById(WRONG_ID));
    }

    @Test
    void getAll_successfullyGetList() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));
        List<UserDto> list = userService.getAll();
        assertNotNull(list);
        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
        assertEquals(userDto, list.get(0));
    }

    @Test
    void create_successfullyCreated() {
        when(userRepository.save(any())).thenReturn(user);
        assertEquals(userService.create(userDto), userDto);
    }

    @Test
    void create_notFoundUser() {
        when(userRepository.save(any())).thenReturn(wrongUser);
        assertThrows(NotFoundException.class, () -> userService.getById(WRONG_ID));
    }

    @Test
    void update_successfullyUpdated() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(mapper.toUser(updatedUserDto)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        UserDto updatedUser = userService.update(updatedUserDto, USER_ID);
        assertNotNull(updatedUser);
        assertEquals(userDto.getName(), updatedUser.getName());
        assertEquals(userDto.getEmail(), updatedUser.getEmail());
        assertEquals(userDto.getId(), updatedUser.getId());
    }

    @Test
    void update_notFoundUserId() {
        when(userRepository.findById(anyLong())).thenThrow(new NotFoundException("fail: user Not Found!"));

        Exception exception = assertThrows(NotFoundException.class,
                () -> userService.update(userDto, WRONG_ID));
        assertEquals(exception.getMessage(), "fail: user Not Found!");
    }

    @Test
    void update_alreadyExistEmailFail() {
        UserDto failUserDto = new UserDto(WRONG_ID, "dailUser", "user@user.user");
        when(userRepository.findById(anyLong())).thenThrow(new AlreadyExistsException("fail: email Is Already Taken!"));

        Exception exception = assertThrows(AlreadyExistsException.class,
                () -> userService.update(failUserDto, USER_ID));
        assertEquals(exception.getMessage(), "fail: email Is Already Taken!");
    }

    @Test
    void delete_successfullyDeleted() {
        userService.delete(USER_ID);
        verify(userRepository, times(1)).deleteById(USER_ID);
    }

}
