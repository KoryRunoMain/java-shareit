package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto);

    UserDto saveUser(UserDto userDto, Long id);

    UserDto findUserById(Long userId);

    List<UserDto> findAllUsers();

    void deleteUserById(Long userId);

}
