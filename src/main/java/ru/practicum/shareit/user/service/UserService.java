package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import java.util.List;

public interface UserService {

    UserDto create(UserDto userDto);

    UserDto save(UserDto userDto, Long id);

    UserDto getById(Long userId);

    List<UserDto> getAll();

    void delete(Long userId);

}
