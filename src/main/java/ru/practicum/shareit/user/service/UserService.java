package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import java.util.List;

public interface UserService {

    UserDto getById(Long userId);

    List<UserDto> getAll();

    UserDto create(UserDto userDto);

    UserDto save(UserDto userDto, Long id);

    void delete(Long userId);

}
