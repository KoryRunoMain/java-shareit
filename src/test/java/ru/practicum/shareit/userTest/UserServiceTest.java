package ru.practicum.shareit.userTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
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
    private UserRepository repository;

    @InjectMocks
    private UserServiceImpl service;

    private static final Long USER_ID = 1L;

    //User
    private final User user = new User(USER_ID, "user", "user@user.user");
    private final User wrongUser = new User(5L, null, null);
    private final UserDto userDto = new UserDto(USER_ID, "user", "user@user.user");
    private final UserDto wrongUserDto = new UserDto(5L, null, null);
    private final UserDto updatedUserDto = new UserDto(USER_ID, "updatedUser", "user@user.user");

    @BeforeEach
    void setUp() {
        lenient().when(mapper.toUserDto(any(User.class))).thenReturn(userDto);
        lenient().when(mapper.toUser(any(UserDto.class))).thenReturn(user);
    }

    @Test
    void test_1_getById_And_ReturnUser() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(user));
        assertEquals(userDto, service.getById(1L));
    }

    @Test
    void test_2_getByWrongId_And_ReturnErrorMessage() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.getById(100L));
    }

    @Test
    void test_3_getAll_And_ReturnUserList() {
        when(repository.findAll()).thenReturn(Collections.singletonList(user));
        List<UserDto> list = service.getAll();
        assertNotNull(list);
        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
        assertEquals(userDto, list.get(0));
    }

    @Test
    void test_4_create_And_ReturnUser() {
        when(repository.save(any())).thenReturn(user);
        assertEquals(service.create(userDto), userDto);
    }

    @Test
    void test_5_createWithWrongData_And_ReturnException() {
        when(repository.save(any())).thenReturn(wrongUser);
        assertThrows(NotFoundException.class, () -> service.getById(5L));
    }

    @Test
    void test_6_update_And_ReturnUser() {
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(mapper.toUser(updatedUserDto)).thenReturn(user);
        when(repository.save(any(User.class))).thenReturn(user);
        UserDto updatedUser = service.update(updatedUserDto, 1L);
        assertNotNull(updatedUser);
        assertEquals(userDto.getName(), updatedUser.getName());
        assertEquals(userDto.getEmail(), updatedUser.getEmail());
        assertEquals(userDto.getId(), updatedUser.getId());
    }

    @Test
    void test_7_delete_And_ReturnStatusOk() {
        Long userId = 1L;
        service.delete(userId);
        verify(repository, times(1)).deleteById(userId);
    }

}
